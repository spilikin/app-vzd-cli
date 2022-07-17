//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.07.15 at 12:12:56 PM CEST 
//


package generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for UserCertificateType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UserCertificateType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dn" type="{}DistinguishedNameType" minOccurs="0"/&gt;
 *         &lt;element name="entryType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="telematikID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="professionOID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="usage" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="userCertificate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserCertificateType", propOrder = {
    "dn",
    "entryType",
    "telematikID",
    "professionOID",
    "usage",
    "userCertificate",
    "description"
})
public class UserCertificateType {

    protected DistinguishedNameType dn;
    protected String entryType;
    protected String telematikID;
    protected String professionOID;
    protected List<String> usage;
    protected String userCertificate;
    protected String description;

    /**
     * Gets the value of the dn property.
     * 
     * @return
     *     possible object is
     *     {@link DistinguishedNameType }
     *     
     */
    public DistinguishedNameType getDn() {
        return dn;
    }

    /**
     * Sets the value of the dn property.
     * 
     * @param value
     *     allowed object is
     *     {@link DistinguishedNameType }
     *     
     */
    public void setDn(DistinguishedNameType value) {
        this.dn = value;
    }

    /**
     * Gets the value of the entryType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntryType() {
        return entryType;
    }

    /**
     * Sets the value of the entryType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntryType(String value) {
        this.entryType = value;
    }

    /**
     * Gets the value of the telematikID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelematikID() {
        return telematikID;
    }

    /**
     * Sets the value of the telematikID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelematikID(String value) {
        this.telematikID = value;
    }

    /**
     * Gets the value of the professionOID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfessionOID() {
        return professionOID;
    }

    /**
     * Sets the value of the professionOID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfessionOID(String value) {
        this.professionOID = value;
    }

    /**
     * Gets the value of the usage property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the usage property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUsage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getUsage() {
        if (usage == null) {
            usage = new ArrayList<String>();
        }
        return this.usage;
    }

    /**
     * Gets the value of the userCertificate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserCertificate() {
        return userCertificate;
    }

    /**
     * Sets the value of the userCertificate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserCertificate(String value) {
        this.userCertificate = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

}
