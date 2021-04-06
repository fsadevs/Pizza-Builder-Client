gdjs.evtsExt__SpaceShooterControls__ShouldTransform = {};
gdjs.evtsExt__SpaceShooterControls__ShouldTransform.GDChangeButtonObjects1= [];
gdjs.evtsExt__SpaceShooterControls__ShouldTransform.GDChangeButtonObjects2= [];

gdjs.evtsExt__SpaceShooterControls__ShouldTransform.conditionTrue_0 = {val:false};
gdjs.evtsExt__SpaceShooterControls__ShouldTransform.condition0IsTrue_0 = {val:false};
gdjs.evtsExt__SpaceShooterControls__ShouldTransform.condition1IsTrue_0 = {val:false};
gdjs.evtsExt__SpaceShooterControls__ShouldTransform.condition2IsTrue_0 = {val:false};


gdjs.evtsExt__SpaceShooterControls__ShouldTransform.mapOfGDgdjs_46evtsExt_95_95SpaceShooterControls_95_95ShouldTransform_46GDChangeButtonObjects1Objects = Hashtable.newFrom({"ChangeButton": gdjs.evtsExt__SpaceShooterControls__ShouldTransform.GDChangeButtonObjects1});gdjs.evtsExt__SpaceShooterControls__ShouldTransform.eventsList0 = function(runtimeScene, eventsFunctionContext) {

{

gdjs.copyArray(eventsFunctionContext.getObjects("ChangeButton"), gdjs.evtsExt__SpaceShooterControls__ShouldTransform.GDChangeButtonObjects1);

gdjs.evtsExt__SpaceShooterControls__ShouldTransform.condition0IsTrue_0.val = false;
gdjs.evtsExt__SpaceShooterControls__ShouldTransform.condition1IsTrue_0.val = false;
{
gdjs.evtsExt__SpaceShooterControls__ShouldTransform.condition0IsTrue_0.val = gdjs.evtTools.input.cursorOnObject(gdjs.evtsExt__SpaceShooterControls__ShouldTransform.mapOfGDgdjs_46evtsExt_95_95SpaceShooterControls_95_95ShouldTransform_46GDChangeButtonObjects1Objects, runtimeScene, false, false);
}if ( gdjs.evtsExt__SpaceShooterControls__ShouldTransform.condition0IsTrue_0.val ) {
{
gdjs.evtsExt__SpaceShooterControls__ShouldTransform.condition1IsTrue_0.val = gdjs.evtTools.input.isMouseButtonPressed(runtimeScene, "Left");
}}
if (gdjs.evtsExt__SpaceShooterControls__ShouldTransform.condition1IsTrue_0.val) {
{if (typeof eventsFunctionContext !== 'undefined') { eventsFunctionContext.returnValue = true; }}}

}


{


gdjs.evtsExt__SpaceShooterControls__ShouldTransform.condition0IsTrue_0.val = false;
{
gdjs.evtsExt__SpaceShooterControls__ShouldTransform.condition0IsTrue_0.val = gdjs.evtTools.input.isKeyPressed(runtimeScene, "Return");
}if (gdjs.evtsExt__SpaceShooterControls__ShouldTransform.condition0IsTrue_0.val) {
{if (typeof eventsFunctionContext !== 'undefined') { eventsFunctionContext.returnValue = true; }}}

}


};

gdjs.evtsExt__SpaceShooterControls__ShouldTransform.func = function(runtimeScene, ChangeButton, parentEventsFunctionContext) {
var eventsFunctionContext = {
  _objectsMap: {
"ChangeButton": ChangeButton
},
  _objectArraysMap: {
"ChangeButton": gdjs.objectsListsToArray(ChangeButton)
},
  _behaviorNamesMap: {
},
  getObjects: function(objectName) {
    return eventsFunctionContext._objectArraysMap[objectName] || [];
  },
  getObjectsLists: function(objectName) {
    return eventsFunctionContext._objectsMap[objectName] || null;
  },
  getBehaviorName: function(behaviorName) {
    return eventsFunctionContext._behaviorNamesMap[behaviorName];
  },
  createObject: function(objectName) {
    var objectsList = eventsFunctionContext._objectsMap[objectName];
    if (objectsList) {
      const object = parentEventsFunctionContext ?
        parentEventsFunctionContext.createObject(objectsList.firstKey()) :
        runtimeScene.createObject(objectsList.firstKey());
      if (object) {
        objectsList.get(objectsList.firstKey()).push(object);
        eventsFunctionContext._objectArraysMap[objectName].push(object);
      }
      return object;    }
    return null;
  },
  getLayer: function(layerName) {
    return runtimeScene.getLayer(layerName);
  },
  getArgument: function(argName) {
    return "";
  },
  getOnceTriggers: function() { return runtimeScene.getOnceTriggers(); }
};

gdjs.evtsExt__SpaceShooterControls__ShouldTransform.GDChangeButtonObjects1.length = 0;
gdjs.evtsExt__SpaceShooterControls__ShouldTransform.GDChangeButtonObjects2.length = 0;

gdjs.evtsExt__SpaceShooterControls__ShouldTransform.eventsList0(runtimeScene, eventsFunctionContext);
return !!eventsFunctionContext.returnValue;
}

