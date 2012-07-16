package sh.calaba.driver.client;

import java.io.File;
import java.util.Map;

import sh.calaba.driver.CalabashCapabilities;
import sh.calaba.driver.client.model.impl.ButtonImpl;
import sh.calaba.driver.client.model.impl.L10nElementImpl;
import sh.calaba.driver.client.model.impl.ListItemImpl;
import sh.calaba.driver.client.model.impl.NativeSearchImpl;
import sh.calaba.driver.client.model.impl.SpinnerImpl;
import sh.calaba.driver.client.model.impl.TextFieldImpl;
import sh.calaba.driver.client.model.impl.ViewImpl;
import sh.calaba.driver.client.model.impl.WaitingSupportImpl;
import sh.calaba.driver.client.model.impl.WebViewImpl;
import sh.calaba.driver.model.ButtonSupport;
import sh.calaba.driver.model.By;
import sh.calaba.driver.model.CalabashAndroidDriver;
import sh.calaba.driver.model.L10nSupport;
import sh.calaba.driver.model.ListItemSupport;
import sh.calaba.driver.model.NativeSearchSupport;
import sh.calaba.driver.model.SpinnerSupport;
import sh.calaba.driver.model.TextFieldSupport;
import sh.calaba.driver.model.ViewSupport;
import sh.calaba.driver.model.WaitingSupport;
import sh.calaba.driver.model.WebViewSupport;

public class RemoteCalabashAndroidDriver extends CalabashAndroidDriver {

  public RemoteCalabashAndroidDriver(String remoteURL, Map<String, Object> capabilities) {
    super(remoteURL, capabilities);
  }

  public RemoteCalabashAndroidDriver(String host, Integer port, CalabashCapabilities capa) {
    super("http://" + host + ":" + port + "/wd/hub", capa.getRawCapabilities());
  }

  public TextFieldSupport textField(By by) {
    return new TextFieldImpl(this, by);
  }

  public ButtonSupport button(By by) {
    return new ButtonImpl(this, by);
  }

  public SpinnerSupport spinner(By by){
    return new SpinnerImpl(this, by);
  }
  
  public WebViewSupport webview(By.CSS css){
    return new WebViewImpl(this, css);
  }
  
  public ListItemSupport listItem(By by) {
    return new ListItemImpl(this, by);
  }

  public WaitingSupport waitFor() {
    return new WaitingSupportImpl(this);
  }

  public ViewSupport findViewById(String id) {
    return new ViewImpl(this, (By.Id) By.id(id));
  }

  public L10nSupport l10nElement(By.L10nElement id) {
    return new L10nElementImpl(this, id);
  }


  public File takeScreenshot(String path) {
    return new ViewImpl(this).takeScreenshot(path);
  }

  public void scrollUp() {
    new ViewImpl(this).scrollUp();
  }

  public NativeSearchSupport searchFor(By by) {
    return new NativeSearchImpl(this, (By.Id) by);
  }

  public void scrollDown() {
    new ViewImpl(this).scrollDown();
  }

  public void waitForText(String text) {
    new ViewImpl(this).waitFor(By.text(text));
  }

  public void waitForView(String name) {
    new ViewImpl(this).waitFor(By.id(name));
  }

  public void pressContextMenuEntry(String text) {
    new ViewImpl(this).pressContextMenuItem(By.text(text));
  }
}
