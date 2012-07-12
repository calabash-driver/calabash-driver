package sh.calaba.driver.model;

public class By {
  private String identifier;

  private By() {}

  protected By(String identifier) {
    this.identifier = identifier;
  }

  public String getIndentifier() {
    return identifier;
  }

  public class Index extends By {
    public Index(Integer identifier) {
      super(String.valueOf(identifier));
    }

    public Index(String identifier) {
      super(identifier);
    }
  }

  public class Id extends By {
    public Id(String id) {
      super(id);
    }
  }

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

  public class ContentDescription extends By {
    public ContentDescription(String text) {
      super(text);
    }
  }

  public enum L10nElementType {
    BUTTON, TOOGLE_BUTTON, MENU_ITEM;
  }

  public static By index(Integer index) {
    return new By().new Index(index);
  }

  public static By index(String index) {
    return new By().new Index(index);
  }

  public static By text(String text) {
    return new By().new ContentDescription(text);
  }

  public static By id(String id) {
    return new By().new Id(id);
  }

  public static By.L10nElement l10nElement(String id) {
    return new By().new L10nElement(id);
  }

  public static By.L10nElement l10nButton(String id) {
    return new By().new L10nElement(id, L10nElementType.BUTTON);
  }

  public static By.L10nElement l10nToggleButton(String id) {
    return new By().new L10nElement(id, L10nElementType.TOOGLE_BUTTON);
  }

  public static By.L10nElement l10nMenuItem(String id) {
    return new By().new L10nElement(id, L10nElementType.MENU_ITEM);
  }
}
