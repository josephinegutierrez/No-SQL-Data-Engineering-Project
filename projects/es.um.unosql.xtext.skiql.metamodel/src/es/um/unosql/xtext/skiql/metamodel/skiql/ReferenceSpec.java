/**
 */
package es.um.unosql.xtext.skiql.metamodel.skiql;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Reference Spec</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link es.um.unosql.xtext.skiql.metamodel.skiql.ReferenceSpec#getVariationFilter <em>Variation Filter</em>}</li>
 * </ul>
 *
 * @see es.um.unosql.xtext.skiql.metamodel.skiql.SkiqlPackage#getReferenceSpec()
 * @model
 * @generated
 */
public interface ReferenceSpec extends RelationSpec {
	/**
	 * Returns the value of the '<em><b>Variation Filter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Variation Filter</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Variation Filter</em>' containment reference.
	 * @see #setVariationFilter(VariationFilter)
	 * @see es.um.unosql.xtext.skiql.metamodel.skiql.SkiqlPackage#getReferenceSpec_VariationFilter()
	 * @model containment="true"
	 * @generated
	 */
	VariationFilter getVariationFilter();

	/**
	 * Sets the value of the '{@link es.um.unosql.xtext.skiql.metamodel.skiql.ReferenceSpec#getVariationFilter <em>Variation Filter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Variation Filter</em>' containment reference.
	 * @see #getVariationFilter()
	 * @generated
	 */
	void setVariationFilter(VariationFilter value);

} // ReferenceSpec
