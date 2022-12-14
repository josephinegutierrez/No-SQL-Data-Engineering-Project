/**
 */
package es.um.nosql.s13e.entitydifferentiation.DecisionTree.impl;

import es.um.nosql.s13e.NoSQLSchema.NoSQLSchemaPackage;

import es.um.nosql.s13e.entitydifferentiation.DecisionTree.DecisionTreeFactory;
import es.um.nosql.s13e.entitydifferentiation.DecisionTree.DecisionTreeForEntity;
import es.um.nosql.s13e.entitydifferentiation.DecisionTree.DecisionTreeNode;
import es.um.nosql.s13e.entitydifferentiation.DecisionTree.DecisionTreePackage;
import es.um.nosql.s13e.entitydifferentiation.DecisionTree.DecisionTrees;
import es.um.nosql.s13e.entitydifferentiation.DecisionTree.IntermediateNode;
import es.um.nosql.s13e.entitydifferentiation.DecisionTree.LeafNode;
import es.um.nosql.s13e.entitydifferentiation.DecisionTree.PropertySpec2;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DecisionTreePackageImpl extends EPackageImpl implements DecisionTreePackage {
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass decisionTreeNodeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass leafNodeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass intermediateNodeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass decisionTreeForEntityEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass decisionTreesEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass propertySpec2EClass = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with
   * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
   * package URI value.
   * <p>Note: the correct way to create the package is via the static
   * factory method {@link #init init()}, which also performs
   * initialization of the package, or returns the registered package,
   * if one already exists.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see es.um.nosql.s13e.entitydifferentiation.DecisionTree.DecisionTreePackage#eNS_URI
   * @see #init()
   * @generated
   */
  private DecisionTreePackageImpl() {
    super(eNS_URI, DecisionTreeFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
   *
   * <p>This method is used to initialize {@link DecisionTreePackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static DecisionTreePackage init() {
    if (isInited) return (DecisionTreePackage)EPackage.Registry.INSTANCE.getEPackage(DecisionTreePackage.eNS_URI);

    // Obtain or create and register package
    Object registeredDecisionTreePackage = EPackage.Registry.INSTANCE.get(eNS_URI);
    DecisionTreePackageImpl theDecisionTreePackage = registeredDecisionTreePackage instanceof DecisionTreePackageImpl ? (DecisionTreePackageImpl)registeredDecisionTreePackage : new DecisionTreePackageImpl();

    isInited = true;

    // Initialize simple dependencies
    NoSQLSchemaPackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theDecisionTreePackage.createPackageContents();

    // Initialize created meta-data
    theDecisionTreePackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theDecisionTreePackage.freeze();

    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(DecisionTreePackage.eNS_URI, theDecisionTreePackage);
    return theDecisionTreePackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getDecisionTreeNode() {
    return decisionTreeNodeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getDecisionTreeNode_YesBranch() {
    return (EReference)decisionTreeNodeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getDecisionTreeNode_NoBranch() {
    return (EReference)decisionTreeNodeEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getLeafNode() {
    return leafNodeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getLeafNode_IdentifiedVariation() {
    return (EReference)leafNodeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getIntermediateNode() {
    return intermediateNodeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getIntermediateNode_CheckedProperty() {
    return (EReference)intermediateNodeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getDecisionTreeForEntity() {
    return decisionTreeForEntityEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getDecisionTreeForEntity_Root() {
    return (EReference)decisionTreeForEntityEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getDecisionTreeForEntity_Entity() {
    return (EReference)decisionTreeForEntityEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getDecisionTrees() {
    return decisionTreesEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getDecisionTrees_Trees() {
    return (EReference)decisionTreesEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getDecisionTrees_Name() {
    return (EAttribute)decisionTreesEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getPropertySpec2() {
    return propertySpec2EClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getPropertySpec2_NeedsTypeCheck() {
    return (EAttribute)propertySpec2EClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getPropertySpec2_Property() {
    return (EReference)propertySpec2EClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DecisionTreeFactory getDecisionTreeFactory() {
    return (DecisionTreeFactory)getEFactoryInstance();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents() {
    if (isCreated) return;
    isCreated = true;

    // Create classes and their features
    decisionTreeNodeEClass = createEClass(DECISION_TREE_NODE);
    createEReference(decisionTreeNodeEClass, DECISION_TREE_NODE__YES_BRANCH);
    createEReference(decisionTreeNodeEClass, DECISION_TREE_NODE__NO_BRANCH);

    leafNodeEClass = createEClass(LEAF_NODE);
    createEReference(leafNodeEClass, LEAF_NODE__IDENTIFIED_VARIATION);

    intermediateNodeEClass = createEClass(INTERMEDIATE_NODE);
    createEReference(intermediateNodeEClass, INTERMEDIATE_NODE__CHECKED_PROPERTY);

    decisionTreeForEntityEClass = createEClass(DECISION_TREE_FOR_ENTITY);
    createEReference(decisionTreeForEntityEClass, DECISION_TREE_FOR_ENTITY__ROOT);
    createEReference(decisionTreeForEntityEClass, DECISION_TREE_FOR_ENTITY__ENTITY);

    decisionTreesEClass = createEClass(DECISION_TREES);
    createEReference(decisionTreesEClass, DECISION_TREES__TREES);
    createEAttribute(decisionTreesEClass, DECISION_TREES__NAME);

    propertySpec2EClass = createEClass(PROPERTY_SPEC2);
    createEAttribute(propertySpec2EClass, PROPERTY_SPEC2__NEEDS_TYPE_CHECK);
    createEReference(propertySpec2EClass, PROPERTY_SPEC2__PROPERTY);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model.  This
   * method is guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void initializePackageContents() {
    if (isInitialized) return;
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Obtain other dependent packages
    NoSQLSchemaPackage theNoSQLSchemaPackage = (NoSQLSchemaPackage)EPackage.Registry.INSTANCE.getEPackage(NoSQLSchemaPackage.eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    leafNodeEClass.getESuperTypes().add(this.getDecisionTreeNode());
    intermediateNodeEClass.getESuperTypes().add(this.getDecisionTreeNode());

    // Initialize classes, features, and operations; add parameters
    initEClass(decisionTreeNodeEClass, DecisionTreeNode.class, "DecisionTreeNode", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getDecisionTreeNode_YesBranch(), this.getDecisionTreeNode(), null, "yesBranch", null, 0, 1, DecisionTreeNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getDecisionTreeNode_NoBranch(), this.getDecisionTreeNode(), null, "noBranch", null, 0, 1, DecisionTreeNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(leafNodeEClass, LeafNode.class, "LeafNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getLeafNode_IdentifiedVariation(), theNoSQLSchemaPackage.getStructuralVariation(), null, "identifiedVariation", null, 1, 1, LeafNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(intermediateNodeEClass, IntermediateNode.class, "IntermediateNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIntermediateNode_CheckedProperty(), this.getPropertySpec2(), null, "checkedProperty", null, 1, 1, IntermediateNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(decisionTreeForEntityEClass, DecisionTreeForEntity.class, "DecisionTreeForEntity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getDecisionTreeForEntity_Root(), this.getDecisionTreeNode(), null, "root", null, 0, 1, DecisionTreeForEntity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getDecisionTreeForEntity_Entity(), theNoSQLSchemaPackage.getEntityType(), null, "entity", null, 1, 1, DecisionTreeForEntity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(decisionTreesEClass, DecisionTrees.class, "DecisionTrees", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getDecisionTrees_Trees(), this.getDecisionTreeForEntity(), null, "trees", null, 0, -1, DecisionTrees.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDecisionTrees_Name(), ecorePackage.getEString(), "name", null, 0, 1, DecisionTrees.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(propertySpec2EClass, PropertySpec2.class, "PropertySpec2", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getPropertySpec2_NeedsTypeCheck(), ecorePackage.getEBoolean(), "needsTypeCheck", null, 1, 1, PropertySpec2.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getPropertySpec2_Property(), theNoSQLSchemaPackage.getProperty(), null, "property", null, 1, 1, PropertySpec2.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Create resource
    createResource(eNS_URI);
  }

} //DecisionTreePackageImpl
