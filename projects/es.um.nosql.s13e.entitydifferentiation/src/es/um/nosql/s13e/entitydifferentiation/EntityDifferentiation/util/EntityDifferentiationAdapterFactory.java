/**
 */
package es.um.nosql.s13e.entitydifferentiation.EntityDifferentiation.util;

import es.um.nosql.s13e.entitydifferentiation.EntityDifferentiation.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see es.um.nosql.s13e.entitydifferentiation.EntityDifferentiation.EntityDifferentiationPackage
 * @generated
 */
public class EntityDifferentiationAdapterFactory extends AdapterFactoryImpl {
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static EntityDifferentiationPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EntityDifferentiationAdapterFactory() {
    if (modelPackage == null) {
      modelPackage = EntityDifferentiationPackage.eINSTANCE;
    }
  }

  /**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
   * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
   * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
  @Override
  public boolean isFactoryForType(Object object) {
    if (object == modelPackage) {
      return true;
    }
    if (object instanceof EObject) {
      return ((EObject)object).eClass().getEPackage() == modelPackage;
    }
    return false;
  }

  /**
   * The switch that delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected EntityDifferentiationSwitch<Adapter> modelSwitch =
    new EntityDifferentiationSwitch<Adapter>() {
      @Override
      public Adapter caseEntityDifferentiation(EntityDifferentiation object) {
        return createEntityDifferentiationAdapter();
      }
      @Override
      public Adapter caseEntityDiff(EntityDiff object) {
        return createEntityDiffAdapter();
      }
      @Override
      public Adapter casePropertySpec(PropertySpec object) {
        return createPropertySpecAdapter();
      }
      @Override
      public Adapter caseStructuralVariationDiff(StructuralVariationDiff object) {
        return createStructuralVariationDiffAdapter();
      }
      @Override
      public Adapter defaultCase(EObject object) {
        return createEObjectAdapter();
      }
    };

  /**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
  @Override
  public Adapter createAdapter(Notifier target) {
    return modelSwitch.doSwitch((EObject)target);
  }


  /**
   * Creates a new adapter for an object of class '{@link es.um.nosql.s13e.entitydifferentiation.EntityDifferentiation.EntityDifferentiation <em>Entity Differentiation</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see es.um.nosql.s13e.entitydifferentiation.EntityDifferentiation.EntityDifferentiation
   * @generated
   */
  public Adapter createEntityDifferentiationAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link es.um.nosql.s13e.entitydifferentiation.EntityDifferentiation.EntityDiff <em>Entity Diff</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see es.um.nosql.s13e.entitydifferentiation.EntityDifferentiation.EntityDiff
   * @generated
   */
  public Adapter createEntityDiffAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link es.um.nosql.s13e.entitydifferentiation.EntityDifferentiation.PropertySpec <em>Property Spec</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see es.um.nosql.s13e.entitydifferentiation.EntityDifferentiation.PropertySpec
   * @generated
   */
  public Adapter createPropertySpecAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link es.um.nosql.s13e.entitydifferentiation.EntityDifferentiation.StructuralVariationDiff <em>Structural Variation Diff</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see es.um.nosql.s13e.entitydifferentiation.EntityDifferentiation.StructuralVariationDiff
   * @generated
   */
  public Adapter createStructuralVariationDiffAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
   * This default implementation returns null.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
  public Adapter createEObjectAdapter() {
    return null;
  }

} //EntityDifferentiationAdapterFactory
