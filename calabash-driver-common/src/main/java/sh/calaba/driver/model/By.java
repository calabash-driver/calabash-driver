/*
 * Copyright 2012 calabash-driver committers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package sh.calaba.driver.model;

/**
 * Mechanism used to locate elements within a view. In order to create your own locating mechanisms,
 * it is possible to subclass this class. Inspired by the Selenium By class.
 *
 * @author ddary
 *
 */
public class By {
  private String identifier;

  private By() {}

  protected By(String identifier) {
    this.identifier = identifier;
  }
  /**
   * @return The identifier of the locator.
   */
  public String getIdentifier() {
    return identifier;
  }

  /**
   * Locator based on index of an element.
   *
   * @author ddary
   *
   */
  public class Index extends By {
    public Index(Integer identifier) {
      super(String.valueOf(identifier));
    }

    public Index(String identifier) {
      super(identifier);
    }
  }

  /**
   * Locator based on Android native name / id of an element.
   *
   * @author ddary
   *
   */
  public class Id extends By {
    public Id(String id) {
      super(id);
    }
  }
  /**
   * Locator based CSS selectors for web views.
   *
   * @author ddary
   *
   */
  public class CSS extends By {
    public CSS(String id) {
      super(id);
    }
  }

  /**
   * Locator based on key of an App resource bundle file.
   *
   * @author ddary
   *
   */
  public class L10nElement extends By {
    private L10nElementType type = null;

    public L10nElement(String id) {
      super(id);
    }

    public L10nElement(String id, L10nElementType type) {
      super(id);
      this.type = type;
    }

    public L10nElementType getType() {
      return type;
    }
  }

  /**
   * Locator based on the text of an element.
   *
   * @author ddary
   *
   */
  public class Text extends By {
    public Text(String text) {
      super(text);
    }
  }

  /**
   * Describing the types of supported {@link L10nElement} types.
   *
   * @author ddary
   *
   */
  public enum L10nElementType {
    BUTTON, TOOGLE_BUTTON, MENU_ITEM;
  }

  /**
   *
   * @param index Index of element.
   * @return a By which locates elements via index.
   */
  public static By.Index index(Integer index) {
    return new By().new Index(index);
  }

  /**
   * @param text The text of the element.
   * @return a By which locates elements via text.
   */
  public static By.Text text(String text) {
    return new By().new Text(text);
  }

  /**
   * @param id The Id / native name of the element.
   * @return a By which locates elements via the id.
   */
  public static By.Id id(String id) {
    return new By().new Id(id);
  }

  /**
   * @param id The resource bundle key.
   * @return a By which locates elements via the resource bundle key.
   */
  public static By.L10nElement l10nElement(String id) {
    return new By().new L10nElement(id);
  }

  /**
   * @param id The resource bundle key.
   * @return a By of type button which locates elements via the resource bundle key.
   */
  public static By.L10nElement l10nButton(String id) {
    return new By().new L10nElement(id, L10nElementType.BUTTON);
  }

  /**
   * @param id The resource bundle key.
   * @return a By of type toggle_button which locates elements via the resource bundle key.
   */
  public static By.L10nElement l10nToggleButton(String id) {
    return new By().new L10nElement(id, L10nElementType.TOOGLE_BUTTON);
  }

  /**
   * @param id The resource bundle key.
   * @return a By of type menu item which locates elements via the resource bundle key.
   */
  public static By.L10nElement l10nMenuItem(String id) {
    return new By().new L10nElement(id, L10nElementType.MENU_ITEM);
  }

  /**
   * @param id The css selector.
   * @return a By which locates elements via css selector.
   */
  public static By.CSS css(String css) {
    return new By().new CSS(css);
  }
}
