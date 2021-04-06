gdjs.evtsExt__SpaceShooterControls__ShouldFire = {};

gdjs.evtsExt__SpaceShooterControls__ShouldFire.conditionTrue_0 = {val:false};
gdjs.evtsExt__SpaceShooterControls__ShouldFire.condition0IsTrue_0 = {val:false};
gdjs.evtsExt__SpaceShooterControls__ShouldFire.condition1IsTrue_0 = {val:false};


gdjs.evtsExt__SpaceShooterControls__ShouldFire.eventsList0 = function(runtimeScene, eventsFunctionContext) {

{


gdjs.evtsExt__SpaceShooterControls__ShouldFire.condition0IsTrue_0.val = false;
{
gdjs.evtsExt__SpaceShooterControls__ShouldFire.condition0IsTrue_0.val = gdjs.evtTools.input.isMouseButtonPressed(runtimeScene, "Left");
}if (gdjs.evtsExt__SpaceShooterControls__ShouldFire.condition0IsTrue_0.val) {
{if (typeof eventsFunctionContext !== 'undefined') { eventsFunctionContext.returnValue = true; }}}

}


{


gdjs.evtsExt__SpaceShooterControls__ShouldFire.condition0IsTrue_0.val = false;
{
gdjs.evtsExt__SpaceShooterControls__ShouldFire.condition0IsTrue_0.val = gdjs.evtTools.input.isKeyPressed(runtimeScene, "Space");
}if (gdjs.evtsExt__SpaceShooterControls__ShouldFire.condition0IsTrue_0.val) {
{if (typeof eventsFunctionContext !== 'undefined') { eventsFunctionContext.returnValue = true; }}}

}


};

gdjs.evtsExt__SpaceShooterControls__ShouldFire.func = function(runtimeScene, parentEventsFunctionContext) {
var eventsFunctionContext = {
  _objectsMap: {
},
  _objectArraysMap: {
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


gdjs.evtsExt__SpaceShooterControls__ShouldFire.eventsList0(runtimeScene, eventsFunctionContext);
return !!eventsFunctionContext.returnValue;
}

