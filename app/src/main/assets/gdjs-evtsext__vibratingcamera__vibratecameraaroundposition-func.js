gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition = {};

gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition.conditionTrue_0 = {val:false};
gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition.condition0IsTrue_0 = {val:false};
gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition.condition1IsTrue_0 = {val:false};
gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition.conditionTrue_1 = {val:false};
gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition.condition0IsTrue_1 = {val:false};
gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition.condition1IsTrue_1 = {val:false};


gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition.eventsList0 = function(runtimeScene, eventsFunctionContext) {

{


gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition.condition0IsTrue_0.val = false;
{
gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition.condition0IsTrue_0.val = gdjs.evtTools.runtimeScene.timerElapsedTime(runtimeScene, 1 / (typeof eventsFunctionContext !== 'undefined' ? Number(eventsFunctionContext.getArgument("Frequency")) || 0 : 0), "CameraVibrationTimer");
}if (gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition.condition0IsTrue_0.val) {
{gdjs.evtTools.camera.setCameraY(runtimeScene, (typeof eventsFunctionContext !== 'undefined' ? Number(eventsFunctionContext.getArgument("YPosition")) || 0 : 0) + gdjs.randomInRange(-((typeof eventsFunctionContext !== 'undefined' ? Number(eventsFunctionContext.getArgument("YAmplitude")) || 0 : 0)), (typeof eventsFunctionContext !== 'undefined' ? Number(eventsFunctionContext.getArgument("YAmplitude")) || 0 : 0)), (typeof eventsFunctionContext !== 'undefined' ? "" + eventsFunctionContext.getArgument("Layer") : ""), 0);
}{gdjs.evtTools.camera.setCameraX(runtimeScene, (typeof eventsFunctionContext !== 'undefined' ? Number(eventsFunctionContext.getArgument("XPosition")) || 0 : 0) + gdjs.randomInRange(-((typeof eventsFunctionContext !== 'undefined' ? Number(eventsFunctionContext.getArgument("XAmplitude")) || 0 : 0)), (typeof eventsFunctionContext !== 'undefined' ? Number(eventsFunctionContext.getArgument("XAmplitude")) || 0 : 0)), (typeof eventsFunctionContext !== 'undefined' ? "" + eventsFunctionContext.getArgument("Layer") : ""), 0);
}{gdjs.evtTools.runtimeScene.resetTimer(runtimeScene, "CameraVibrationTimer");
}}

}


};gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition.eventsList1 = function(runtimeScene, eventsFunctionContext) {

{


gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition.condition0IsTrue_0.val = false;
{
{gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition.conditionTrue_1 = gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition.condition0IsTrue_0;
gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition.conditionTrue_1.val = eventsFunctionContext.getOnceTriggers().triggerOnce(9591700);
}
}if (gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition.condition0IsTrue_0.val) {
{gdjs.evtTools.runtimeScene.resetTimer(runtimeScene, "CameraVibrationTimer");
}}

}


{


gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition.condition0IsTrue_0.val = false;
{
{gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition.conditionTrue_1 = gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition.condition0IsTrue_0;
gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition.conditionTrue_1.val = ((typeof eventsFunctionContext !== 'undefined' ? Number(eventsFunctionContext.getArgument("Frequency")) || 0 : 0) > 0);
}
}if (gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition.condition0IsTrue_0.val) {

{ //Subevents
gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition.eventsList0(runtimeScene, eventsFunctionContext);} //End of subevents
}

}


};

gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition.func = function(runtimeScene, XPosition, YPosition, XAmplitude, YAmplitude, Frequency, Layer, parentEventsFunctionContext) {
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
if (argName === "XPosition") return XPosition;
if (argName === "YPosition") return YPosition;
if (argName === "XAmplitude") return XAmplitude;
if (argName === "YAmplitude") return YAmplitude;
if (argName === "Frequency") return Frequency;
if (argName === "Layer") return Layer;
    return "";
  },
  getOnceTriggers: function() { return runtimeScene.getOnceTriggers(); }
};


gdjs.evtsExt__VibratingCamera__VibrateCameraAroundPosition.eventsList1(runtimeScene, eventsFunctionContext);
return;
}

