/*
 * I_Directory_Administration
 * REST Schnittstelle zur Pflege der Verzeichniseinträge. Über diese Schnittstelle können Verzeichniseinträge inklusive Zertifikaten erzeugt, aktualisiert und gelöscht werden. Die Administration von Fachdaten erfolgt über Schnittstelle I_Directory_Application_Maintenance und wird durch die Fachanwendungen durchgeführt. Lesender Zugriff auf die Fachdaten ist mit Operation getDirectoryEntries in vorliegender Schnittstelle möglich.
 *
 * The version of the OpenAPI document: 1.6.3
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package de.gematik.ti.epa.vzd.client.model;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Objects;

/**
 * FAD1
 */

public class FAD1 {

  public static final String SERIALIZED_NAME_DN = "dn";
  @SerializedName(SERIALIZED_NAME_DN)
  private DistinguishedName dn;

  public static final String SERIALIZED_NAME_KO_M_L_E_VERSION = "KOM-LE_Version";
  @SerializedName(SERIALIZED_NAME_KO_M_L_E_VERSION)
  private String koMLEVersion;

  public static final String SERIALIZED_NAME_MAIL = "mail";
  @SerializedName(SERIALIZED_NAME_MAIL)
  private List<String> mail = null;


  public FAD1 dn(DistinguishedName dn) {

    this.dn = dn;
    return this;
  }

  /**
   * Get dn
   *
   * @return dn
   **/
  @ApiModelProperty(required = true, value = "")

  public DistinguishedName getDn() {
    return dn;
  }


  public void setDn(DistinguishedName dn) {
    this.dn = dn;
  }


  /**
   * Get mail
   *
   * @return mail
   **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public List<String> getMail() {
    return mail;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    FAD1 FAD1 = (FAD1) o;
    return Objects.equals(this.dn, FAD1.dn) &&
        Objects.equals(this.mail, FAD1.mail) &&
        Objects.equals(this.koMLEVersion, FAD1.koMLEVersion);
  }

  public FAD1 koMLEVersion(String koMLEVersion) {

    this.koMLEVersion = koMLEVersion;
    return this;
  }

  /**
   * Die höchste KOM-LE_Version der KIM Clientmodule des KIM Kunden
   *
   * @return koMLEVersion
   **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Die höchste KOM-LE_Version der KIM Clientmodule des KIM Kunden")

  public String getKoMLEVersion() {
    return koMLEVersion;
  }


  public void setKoMLEVersion(String koMLEVersion) {
    this.koMLEVersion = koMLEVersion;
  }

  @Override
  public int hashCode() {
    return Objects.hash(dn, mail, koMLEVersion);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FAD1 {\n");
    sb.append("    dn: ").append(toIndentedString(dn)).append("\n");
    sb.append("    mail: ").append(toIndentedString(mail)).append("\n");
    sb.append("    koMLEVersion: ").append(toIndentedString(koMLEVersion)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first
   * line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

