package org.pentaho.di.trans.steps.cleanse;

import org.pentaho.di.core.injection.Injection;

/**
 * Contains the properties of the fields to clean.
 *
 * @author Nicolas ADMENT
 */
public class Cleanse implements Cloneable {

  public Cleanse() {
    super();
  }

  /** The target field name */  
  private String field;


  private String name;

  private String rule = "None";

  @Override
  public Object clone() {
    Cleanse clone;
    try {
      clone = (Cleanse) super.clone();

    } catch (CloneNotSupportedException e) {
      return null;
    }
    return clone;
  }

  public String getInputField() {
    return field;
  }

  @Injection(name = "INPUT", group = "FIELDS")
  public void setInputField(final String name) {
    this.field = name;
  }

  public String getName() {
    return name;
  }

  @Injection(name = "OUTPUT", group = "FIELDS")
  public void setName(final String name) {
    this.name = name;
  }

  
  public String getRule() {
    return rule;
  }

  @Injection(name = "RULE", group = "FIELDS")
  public void setRule(final String rule) {

    if (rule == null)
      this.rule = "None";
    else
      this.rule = rule;
  }

  @Override
  public String toString() {
    return field + ":" + this.getRule();
  }
}
