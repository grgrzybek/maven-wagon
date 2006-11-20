package org.apache.maven.wagon.providers.ssh.knownhost;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.wagon.Wagon;
import org.apache.maven.wagon.providers.ssh.AbstractSshWagon;
import org.apache.maven.wagon.providers.ssh.TestData;
import org.apache.maven.wagon.repository.Repository;
import org.codehaus.plexus.PlexusTestCase;

/**
 * Generic Unit test for <code>KnownHostsProvider</code>
 *
 * @author Juan F. Codagnone
 * @see org.apache.maven.wagon.providers.ssh.knownhost.KnownHostsProvider
 * @since Sep 12, 2005
 */
public abstract class AbstractKnownHostsProviderTest
    extends PlexusTestCase
{
    protected KnownHostsProvider okHostsProvider;

    protected KnownHostsProvider failHostsProvider;

    protected KnownHostsProvider changedHostsProvider;

    private AbstractSshWagon wagon;

    private Repository source;

    /**
     * tests what happens if the remote host has a different key than the one
     * we expect
     *
     * @throws Exception on error
     */
    public void testIncorrectKey()
        throws Exception
    {
        wagon.setKnownHostsProvider( failHostsProvider );

        try
        {
            wagon.connect( source );

            fail( "Should not have successfully connected - host is not known" );
        }
        catch ( UnknownHostException e )
        {
            // ok
        }
    }

    /**
     * tests what happens if the remote host has changed since being recorded.
     *
     * @throws Exception on error
     */
    public void testChangedKey()
        throws Exception
    {
        wagon.setKnownHostsProvider( changedHostsProvider );

        try
        {
            wagon.connect( source );

            fail( "Should not have successfully connected - host is changed" );
        }
        catch ( KnownHostChangedException e )
        {
            // ok
        }
    }

    /**
     * tests what happens if the remote host has the expected key
     *
     * @throws Exception on error
     */
    public void testCorrectKey()
        throws Exception
    {
        wagon.setKnownHostsProvider( okHostsProvider );

        wagon.connect( source );
    }

    protected void setUp()
        throws Exception
    {
        super.setUp();
        source =
            new Repository( "test", "scp://" + TestData.getUserName() + "@" + TestData.getHostname() + "/tmp/foo" );

        wagon = (AbstractSshWagon) lookup( Wagon.ROLE, "scp" );
        wagon.setInteractive( false );
    }
}