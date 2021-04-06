gdjs.evtsExt__SpaceShooterControls__GoingUp = {};
gdjs.evtsExt__SpaceShooterControls__GoingUp.GDPlayerObjects1= [];
gdjs.evtsExt__SpaceShooterControls__GoingUp.GDPlayerObjects2= [];

gdjs.evtsExt__SpaceShooterControls__GoingUp.conditionTrue_0 = {val:false};
gdjs.evtsExt__SpaceShooterControls__GoingUp.condition0IsTrue_0 = {val:false};
gdjs.evtsExt__SpaceShooterControls__GoingUp.condition1IsTrue_0 = {val:false};
gdjs.evtsExt__SpaceShooterControls__GoingUp.condition2IsTrue_0 = {val:false};


gdjs.evtsExt__SpaceShooterControls__GoingUp.eventsList0 = function(runtimeScene, eventsFunctionContext) {

{

gdjs.copyArray(eventsFunctionContext.getObjects("Player"), gdjs.evtsExt__SpaceShooterControls__GoingUp.GDPlayerObjects1);

gdjs.evtsExt__SpaceShooterControls__GoingUp.condition0IsTrue_0.val = false;
gdjs.evtsExt__SpaceShooterControls__GoingUp.condition1IsTrue_0.val = false;
{
for(var i = 0, k = 0, l = gdjs.evtsExt__SpaceShooterControls__GoingUp.GDPlayerObjects1.length;i<l;++i) {
    if ( gdjs.evtsExt__SpaceShooterControls__GoingUp.GDPlayerObjects1[i].getVariableNumber(gdjs.evtsExt__SpaceShooterControls__GoingUp.GDPlayerObjects1[i].getVariables().get("GoingUp")) == 1 ) {
        gdjs.evtsExt__SpaceShooterControls__GoingUp.condition0IsTrue_0.val = true;
        gdjs.evtsExt__SpaceShooterControls__GoingUp.GDPlayerObjects1[k] = gdjs.evtsExt__SpaceShooterControls__GoingUp.GDPlayerObjects1[i];
        ++k;
    }
}
gdjs.evtsExt__SpaceShooterControls__GoingUp.GDPlayerObjects1.length = k;}if ( gdjs.evtsExt__SpaceShooterControls__GoingUp.condition0IsTrue_0.val ) {
{
for(var i = 0, k = 0, l = gdjs.evtsExt__SpaceShooterControls__GoingUp.GDPlayerObjects1.length;i<l;++i) {
    if ( gdjs.evtsExt__SpaceShooterControls__GoingUp.GDPlayerObjects1[i].getVariableNumber(gdjs.evtsExt__SpaceShooterControls__GoingUp.GDPlayerObjects1[i].getVariables().get("GoingDown")) == 0 ) {
        gdjs.evtsExt__SpaceShooterControls__GoingUp.condition1IsTrue_0.val = true;
        gdjs.evtsExt__SpaceShooterControls__GoingUp.GDPlayerObjects1[k] = gdjs.evtsExt__SpaceShooterControls__GoingUp.GDPlayerObjects1[i];
        ++k;
    }
}
gdjs.evtsExt__SpaceShooterControls__GoingUp.GDPlayerObjects1.length = k;}}
if (gdjs.evtsExt__SpaceShooterControls__GoingUp.condition1IsTrue_0.val) {
{if (typeof eventsFunctionContext !== 'undefined') { eventsFunctionContext.returnValue = true; }}}

}


};

gdjs.evtsExt__SpaceShooterControls__GoingUp.func = function(runtimeScene, Player, parentEventsFunctionContext) {
var eventsFunctionContext = {
  _objectsMap: {
"Player": Player
},
  _objectArraysMap: {
"Player": gdjs.objectsListsToArray(Player)
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

gdjs.evtsExt__SpaceShooterControls__GoingUp.GDPlayerObjects1.length = 0;
gdjs.evtsExt__SpaceShooterControls__GoingUp.GDPlayerObjects2.length = 0;

gdjs.evtsExt__SpaceShooterControls__GoingUp.eventsList0(runtimeScene, eventsFunctionContext);
return !!eventsFunctionContext.returnValue;
}

