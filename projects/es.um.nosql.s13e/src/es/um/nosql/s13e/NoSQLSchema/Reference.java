/**
 */
package es.um.nosql.s13e.NoSQLSchema;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link es.um.nosql.s13e.NoSQLSchema.Reference#getOpposite <em>Opposite</em>}</li>
 *   <li>{@link es.um.nosql.s13e.NoSQLSchema.Reference#getRefTo <em>Ref To</em>}</li>
 *   <li>{@link es.um.nosql.s13e.NoSQLSchema.Reference#getOriginalType <em>Original Type</em>}</li>
 *   <li>{@link es.um.nosql.s13e.NoSQLSchema.Reference#getProperties <em>Properties</em>}</li>
 * </ul>
 *
 * @see es.um.nosql.s13e.NoSQLSchema.NoSQLSchemaPackage#getReference()
 * @model
 * @generated
 */
public interface Reference extends Association
{
  /**
   * Returns the value of the '<em><b>Opposite</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Opposite</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Opposite</em>' reference.
   * @see #setOpposite(Reference)
   * @see es.um.nosql.s13e.NoSQLSchema.NoSQLSchemaPackage#getReference_Opposite()
   * @model
   * @generated
   */
  Reference getOpposite();

  /**
   * Sets the value of the '{@link es.um.nosql.s13e.NoSQLSchema.Reference#getOpposite <em>Opposite</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Opposite</em>' reference.
   * @see #getOpposite()
   * @generated
   */
  void setOpposite(Reference value);

  /**
   * Returns the value of the '<em><b>Ref To</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Ref To</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Ref To</em>' reference.
   * @see #setRefTo(Entity)
   * @see es.um.nosql.s13e.NoSQLSchema.NoSQLSchemaPackage#getReference_RefTo()
   * @model required="true"
   * @generated
   */
  Entity getRefTo();

  /**
   * Sets the value of the '{@link es.um.nosql.s13e.NoSQLSchema.Reference#getRefTo <em>Ref To</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Ref To</em>' reference.
   * @see #getRefTo()
   * @generated
   */
  void setRefTo(Entity value);

  /**
   * Returns the value of the '<em><b>Original Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Original Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Original Type</em>' attribute.
   * @see #setOriginalType(String)
   * @see es.um.nosql.s13e.NoSQLSchema.NoSQLSchemaPackage#getReference_OriginalType()
   * @model
   * @generated
   */
  String getOriginalType();

  /**
   * Sets the value of the '{@link es.um.nosql.s13e.NoSQLSchema.Reference#getOriginalType <em>Original Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Original Type</em>' attribute.
   * @see #getOriginalType()
   * @generated
   */
  void setOriginalType(String value);

  /**
   * Returns the value of the '<em><b>Properties</b></em>' containment reference list.
   * The list contents are of type {@link es.um.nosql.s13e.NoSQLSchema.Property}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Properties</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Properties</em>' containment reference list.
   * @see es.um.nosql.s13e.NoSQLSchema.NoSQLSchemaPackage#getReference_Properties()
   * @model containment="true"
   * @generated
   */
  EList<Property> getProperties();

} // Reference
