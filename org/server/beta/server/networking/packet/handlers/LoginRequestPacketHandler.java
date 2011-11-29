/*License
====================
Copyright (c) 2010-2012 Daniel Vidmar

We use a modified GNU gpl v 3 license for this.

GNU gpl v 3 is included in License.txt

The modified part of the license is some additions which state the following:

"Redistributions of this project in source or binary must give credit to UnXoft Interactive and DualCraft"
"Redistributions of this project in source or binary must modify at least 300 lines of code in order to release
an initial version. This will require documentation or proof of the 300 modified lines of code."
"Our developers reserve the right to add any additions made to a redistribution of DualCraft into the main
project"
"Our developers reserver the right if they suspect a closed source software using any code from our project
to request to overview the source code of the suspected software. If the owner of the suspected software refuses 
to allow a devloper to overview the code then we shall/are granted the right to persue legal action against
him/her"*/
package dualcraft.org.server.beta.server.networking.packet.handlers;

import dualcraft.org.server.beta.server.model.Universe;
import dualcraft.org.server.beta.server.networking.netty.util.NettyStringHandler;
import dualcraft.org.server.beta.server.networking.packet.AbstractPacketHandler;
import dualcraft.org.server.beta.server.networking.packet.Packet;
import dualcraft.org.server.beta.server.networking.packet.outgoing.KickPacket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;

/**
 * A packet handler that handles login requests
 *
 * 
 * @version 0.9.0.1
 */
public class LoginRequestPacketHandler extends AbstractPacketHandler {

    /**
     * The expected protocol version
     *
     * @since 0.9.0.0
     */
    private final static int expectedProtocolVersion = 17;

    /**
     * Gets the name of this packet handler
     * 
     * @return The name
     * @since 0.9.0.0
     */
    @Override
    public String getName() {
        return "LoginRequestPacketHandler";
    }

    /**
     * Handles the specified packet
     * 
     * @param context The channel handler context to use
     * @param packet The packet to handle
     * @since 0.9.0.1
     */
    @Override
    public void handle(final ChannelHandlerContext context, final Packet packet) {
        //Get the protocol version
        final int protocolVersion = packet.getBuffer().readInt();
        //Get the username
        final String username = NettyStringHandler.readUTF16(packet.getBuffer());

        //Check to see if the user is a hacker (or just stupid)
        if ((Boolean) Universe.getInstance().getSetting("verifyUsernames", true)
                && !Universe.getInstance().getUnverifiedUsernames().containsKey(username)) {
            //Kick the stupid hacker
            new KickPacket(packet.getChannel()).executeKick("Please don't try to hack this server, we're smarter than that");
            
        }

        //Check the protocol version
        if (protocolVersion != expectedProtocolVersion) {
            //Check to see if we verify usernames
            if ((Boolean) Universe.getInstance().getSetting("verifyUsernames", true)) {
                //Username cannot be verified so remove it
                Universe.getInstance().getUnverifiedUsernames().remove(username);
            }
            //Kick the user
            new KickPacket(packet.getChannel()).executeKick("Invalid protocol version: " + protocolVersion + ", expected " + expectedProtocolVersion);
            //Finally, return
            return;
        }

        //Check to see if we verify usernames
        if ((Boolean) Universe.getInstance().getSetting("verifyUsernames", true)) {
            //Get the handshake response string
            final String connectionHash = Universe.getInstance().getUnverifiedUsernames().get(username);

            //Try to open the connection verification
            try {
                //Get the verification URL
                final URL verificationURL = new URL("http://session.minecraft.net/game/checkserver.jsp?user=" + username + "&serverId=" + connectionHash);
                //Open the connection
                final URLConnection verificationConnection = verificationURL.openConnection();
                //Get the reader
                final BufferedReader verificationReader = new BufferedReader(
                        new InputStreamReader(
                        verificationConnection.getInputStream()));
                
                //Set up the input line
                final String inputLine = verificationReader.readLine();

                //Read a non-null line and check the value
                if (inputLine != null && !inputLine.equalsIgnoreCase("YES")) {
                    //Create a kick packet
                    new KickPacket(packet.getChannel()).executeKick("Session verification failed! Try using a real account ;)");
                    //Close and return
                    verificationReader.close();
                    return;
                }
                
                verificationReader.close();
            } catch (IOException e) {
                //Log it
                Logger.getLogger(this.getName()).severe("Minecraft.net is down!");
                //And kick the player
                new KickPacket(packet.getChannel()).executeKick("Minecraft.net is down, try again in a few minutes");
            }

            Universe.getInstance().getUnverifiedUsernames().remove(username);
        }

        //Temporarily kick
        new KickPacket(packet.getChannel()).executeKick("You can log in soon, " + username + "!");

    }
}
