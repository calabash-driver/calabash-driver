package sh.calaba.driver.server.connector;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import sh.calaba.driver.CalabashCapabilities;



public interface CalabashAndroidConnector {

  /**
   * @return The capabilities of the current session.
   */
  public abstract CalabashCapabilities getSessionCapabilities();

  /**
   * Sends the given JSON action command to the connected Calabash server on the device.
   * 
   * @param action The calabash action command to execute.
   * @return The result of the action based on the result of the Calabash server.
   * @throws IOException
   * @throws JSONException
   */
  public abstract JSONObject execute(JSONObject action) throws IOException, JSONException;

  /**
   * Cleans up the resources by closing the open connections.
   */
  public abstract void quit();

  /**
   * Method is actually starting this connector in connecting to the Calabash-server using a socket
   * connection. The connection is in the beginning verified by sending {@link #PING} command and
   * expecting the response {@link #PONG}.
   */
  public abstract void start();

}
