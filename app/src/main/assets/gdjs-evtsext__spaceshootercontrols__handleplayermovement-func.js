gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement = {};
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1= [];
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2= [];
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects3= [];
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDChangeButtonObjects1= [];
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDChangeButtonObjects2= [];
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDChangeButtonObjects3= [];

gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.conditionTrue_0 = {val:false};
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0 = {val:false};
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition1IsTrue_0 = {val:false};
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition2IsTrue_0 = {val:false};


gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.mapOfGDgdjs_46evtsExt_95_95SpaceShooterControls_95_95HandlePlayerMovement_46GDChangeButtonObjects1Objects = Hashtable.newFrom({"ChangeButton": gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDChangeButtonObjects1});gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.eventsList0 = function(runtimeScene, eventsFunctionContext) {

{

gdjs.copyArray(eventsFunctionContext.getObjects("Player"), gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2);

gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val = false;
{
for(var i = 0, k = 0, l = gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2.length;i<l;++i) {
    if ( gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2[i].getX() > gdjs.evtTools.input.getMouseX(runtimeScene, "", 0) + 10 ) {
        gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val = true;
        gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2[k] = gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2[i];
        ++k;
    }
}
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2.length = k;}if (gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val) {
/* Reuse gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2 */
{for(var i = 0, len = gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2.length ;i < len;++i) {
    gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2[i].getBehavior(eventsFunctionContext.getBehaviorName("TopDownMovement")).simulateLeftKey();
}
}}

}


{

gdjs.copyArray(eventsFunctionContext.getObjects("Player"), gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2);

gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val = false;
{
for(var i = 0, k = 0, l = gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2.length;i<l;++i) {
    if ( gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2[i].getX() < gdjs.evtTools.input.getMouseX(runtimeScene, "", 0) - 10 ) {
        gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val = true;
        gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2[k] = gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2[i];
        ++k;
    }
}
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2.length = k;}if (gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val) {
/* Reuse gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2 */
{for(var i = 0, len = gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2.length ;i < len;++i) {
    gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2[i].getBehavior(eventsFunctionContext.getBehaviorName("TopDownMovement")).simulateRightKey();
}
}}

}


{

gdjs.copyArray(eventsFunctionContext.getObjects("Player"), gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2);

gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val = false;
{
for(var i = 0, k = 0, l = gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2.length;i<l;++i) {
    if ( gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2[i].getY() < gdjs.evtTools.input.getMouseY(runtimeScene, "", 0) - 10 ) {
        gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val = true;
        gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2[k] = gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2[i];
        ++k;
    }
}
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2.length = k;}if (gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val) {
/* Reuse gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2 */
{for(var i = 0, len = gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2.length ;i < len;++i) {
    gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2[i].getBehavior(eventsFunctionContext.getBehaviorName("TopDownMovement")).simulateDownKey();
}
}{for(var i = 0, len = gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2.length ;i < len;++i) {
    gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2[i].returnVariable(gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2[i].getVariables().get("GoingDown")).setNumber(1);
}
}}

}


{

gdjs.copyArray(eventsFunctionContext.getObjects("Player"), gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1);

gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val = false;
{
for(var i = 0, k = 0, l = gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1.length;i<l;++i) {
    if ( gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1[i].getY() > gdjs.evtTools.input.getMouseY(runtimeScene, "", 0) + 10 ) {
        gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val = true;
        gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1[k] = gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1[i];
        ++k;
    }
}
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1.length = k;}if (gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val) {
/* Reuse gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1 */
{for(var i = 0, len = gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1.length ;i < len;++i) {
    gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1[i].getBehavior(eventsFunctionContext.getBehaviorName("TopDownMovement")).simulateUpKey();
}
}{for(var i = 0, len = gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1.length ;i < len;++i) {
    gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1[i].returnVariable(gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1[i].getVariables().get("GoingUp")).setNumber(1);
}
}}

}


};gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.eventsList1 = function(runtimeScene, eventsFunctionContext) {

{


{
gdjs.copyArray(eventsFunctionContext.getObjects("Player"), gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1);
{for(var i = 0, len = gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1.length ;i < len;++i) {
    gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1[i].returnVariable(gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1[i].getVariables().get("GoingUp")).setNumber(0);
}
}{for(var i = 0, len = gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1.length ;i < len;++i) {
    gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1[i].returnVariable(gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1[i].getVariables().get("GoingDown")).setNumber(0);
}
}}

}


{



}


{

gdjs.copyArray(eventsFunctionContext.getObjects("ChangeButton"), gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDChangeButtonObjects1);

gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val = false;
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition1IsTrue_0.val = false;
{
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val = gdjs.evtTools.input.isMouseButtonPressed(runtimeScene, "Left");
}if ( gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val ) {
{
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition1IsTrue_0.val = gdjs.evtTools.input.cursorOnObject(gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.mapOfGDgdjs_46evtsExt_95_95SpaceShooterControls_95_95HandlePlayerMovement_46GDChangeButtonObjects1Objects, runtimeScene, true, true);
}}
if (gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition1IsTrue_0.val) {

{ //Subevents
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.eventsList0(runtimeScene, eventsFunctionContext);} //End of subevents
}

}


{



}


{


gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val = false;
{
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val = gdjs.evtTools.input.isKeyPressed(runtimeScene, "Left");
}if (gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val) {
gdjs.copyArray(eventsFunctionContext.getObjects("Player"), gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1);
{for(var i = 0, len = gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1.length ;i < len;++i) {
    gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1[i].getBehavior(eventsFunctionContext.getBehaviorName("TopDownMovement")).simulateLeftKey();
}
}}

}


{


gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val = false;
{
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val = gdjs.evtTools.input.isKeyPressed(runtimeScene, "Right");
}if (gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val) {
gdjs.copyArray(eventsFunctionContext.getObjects("Player"), gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1);
{for(var i = 0, len = gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1.length ;i < len;++i) {
    gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1[i].getBehavior(eventsFunctionContext.getBehaviorName("TopDownMovement")).simulateRightKey();
}
}}

}


{


gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val = false;
{
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val = gdjs.evtTools.input.isKeyPressed(runtimeScene, "Down");
}if (gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val) {
gdjs.copyArray(eventsFunctionContext.getObjects("Player"), gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1);
{for(var i = 0, len = gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1.length ;i < len;++i) {
    gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1[i].getBehavior(eventsFunctionContext.getBehaviorName("TopDownMovement")).simulateDownKey();
}
}{for(var i = 0, len = gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1.length ;i < len;++i) {
    gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1[i].returnVariable(gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1[i].getVariables().get("GoingDown")).setNumber(1);
}
}}

}


{


gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val = false;
{
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val = gdjs.evtTools.input.isKeyPressed(runtimeScene, "Up");
}if (gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.condition0IsTrue_0.val) {
gdjs.copyArray(eventsFunctionContext.getObjects("Player"), gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1);
{for(var i = 0, len = gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1.length ;i < len;++i) {
    gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1[i].getBehavior(eventsFunctionContext.getBehaviorName("TopDownMovement")).simulateUpKey();
}
}{for(var i = 0, len = gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1.length ;i < len;++i) {
    gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1[i].returnVariable(gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1[i].getVariables().get("GoingUp")).setNumber(1);
}
}}

}


};

gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.func = function(runtimeScene, Player, TopDownMovement, ChangeButton, parentEventsFunctionContext) {
var eventsFunctionContext = {
  _objectsMap: {
"Player": Player
, "ChangeButton": ChangeButton
},
  _objectArraysMap: {
"Player": gdjs.objectsListsToArray(Player)
, "ChangeButton": gdjs.objectsListsToArray(ChangeButton)
},
  _behaviorNamesMap: {
"TopDownMovement": TopDownMovement
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

gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects1.length = 0;
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects2.length = 0;
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDPlayerObjects3.length = 0;
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDChangeButtonObjects1.length = 0;
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDChangeButtonObjects2.length = 0;
gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.GDChangeButtonObjects3.length = 0;

gdjs.evtsExt__SpaceShooterControls__HandlePlayerMovement.eventsList1(runtimeScene, eventsFunctionContext);
return;
}

