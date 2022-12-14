/**
 */
package es.um.nosql.s13e.entitydifferentiation.DecisionTree.util;

import es.um.nosql.s13e.entitydifferentiation.DecisionTree.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see es.um.nosql.s13e.entitydifferentiation.DecisionTree.DecisionTreePackage
 * @generated
 */
public class DecisionTreeAdapterFactory extends AdapterFactoryImpl {
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static DecisionTreePackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DecisionTreeAdapterFactory() {
    if (modelPackage == null) {
      modelPackage = DecisionTreePackage.eINSTANCE;
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
  protected DecisionTreeSwitch<Adapter> modelSwitch =
    new DecisionTreeSwitch<Adapter>() {
      @Override
      public Adapter caseDecisionTreeNode(DecisionTreeNode object) {
        return createDecisionTreeNodeAdapter();
      }
      @Override
      public Adapter caseLeafNode(LeafNode object) {
        return createLeafNodeAdapter();
      }
      @Override
      public Adapter caseIntermediateNode(IntermediateNode object) {
        return createIntermediateNodeAdapter();
      }
      @Override
      public Adapter caseDecisionTreeForEntity(DecisionTreeForEntity object) {
        return createDecisionTreeForEntityAdapter();
      }
      @Override
      public Adapter caseDecisionTrees(DecisionTrees object) {
        return createDecisionTreesAdapter();
      }
      @Override
      public Adapter casePropertySpec2(PropertySpec2 object) {
        return createPropertySpec2Adapter();
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
   * Creates a new adapter for an object of class '{@link es.um.nosql.s13e.entitydifferentiation.DecisionTree.DecisionTreeNode <em>Node</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see es.um.nosql.s13e.entitydifferentiation.DecisionTree.DecisionTreeNode
   * @generated
   */
  public Adapter createDecisionTreeNodeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link es.um.nosql.s13e.entitydifferentiation.DecisionTree.LeafNode <em>Leaf Node</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see es.um.nosql.s13e.entitydifferentiation.DecisionTree.LeafNode
   * @generated
   */
  public Adapter createLeafNodeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link es.um.nosql.s13e.entitydifferentiation.DecisionTree.IntermediateNode <em>Intermediate Node</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see es.um.nosql.s13e.entitydifferentiation.DecisionTree.IntermediateNode
   * @generated
   */
  public Adapter createIntermediateNodeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link es.um.nosql.s13e.entitydifferentiation.DecisionTree.DecisionTreeForEntity <em>For Entity</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see es.um.nosql.s13e.entitydifferentiation.DecisionTree.DecisionTreeForEntity
   * @generated
   */
  public Adapter createDecisionTreeForEntityAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link es.um.nosql.s13e.entitydifferentiation.DecisionTree.DecisionTrees <em>Decision Trees</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see es.um.nosql.s13e.entitydifferentiation.DecisionTree.DecisionTrees
   * @generated
   */
  public Adapter createDecisionTreesAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link es.um.nosql.s13e.entitydifferentiation.DecisionTree.PropertySpec2 <em>Property Spec2</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see es.um.nosql.s13e.entitydifferentiation.DecisionTree.PropertySpec2
   * @generated
   */
  public Adapter createPropertySpec2Adapter() {
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

} //DecisionTreeAdapterFactory
