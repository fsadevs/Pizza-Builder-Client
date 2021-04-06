var gdjs;
(function(gdjs2) {
  class PanelSpriteRuntimeObject extends gdjs2.RuntimeObject {
    constructor(runtimeScene, panelSpriteObjectData) {
      super(runtimeScene, panelSpriteObjectData);
      this.opacity = 255;
      this._rBorder = panelSpriteObjectData.rightMargin;
      this._lBorder = panelSpriteObjectData.leftMargin;
      this._tBorder = panelSpriteObjectData.topMargin;
      this._bBorder = panelSpriteObjectData.bottomMargin;
      this._tiled = panelSpriteObjectData.tiled;
      this._width = panelSpriteObjectData.width;
      this._height = panelSpriteObjectData.height;
      this._renderer = new gdjs2.PanelSpriteRuntimeObjectRenderer(this, runtimeScene, panelSpriteObjectData.texture, panelSpriteObjectData.tiled);
      this.onCreated();
    }
    updateFromObjectData(oldObjectData, newObjectData) {
      if (oldObjectData.width !== newObjectData.width) {
        this.setWidth(newObjectData.width);
      }
      if (oldObjectData.height !== newObjectData.height) {
        this.setHeight(newObjectData.height);
      }
      let updateTexture = false;
      if (oldObjectData.rightMargin !== newObjectData.rightMargin) {
        this._rBorder = newObjectData.rightMargin;
        updateTexture = true;
      }
      if (oldObjectData.leftMargin !== newObjectData.leftMargin) {
        this._lBorder = newObjectData.leftMargin;
        updateTexture = true;
      }
      if (oldObjectData.topMargin !== newObjectData.topMargin) {
        this._tBorder = newObjectData.topMargin;
        updateTexture = true;
      }
      if (oldObjectData.bottomMargin !== newObjectData.bottomMargin) {
        this._bBorder = newObjectData.bottomMargin;
        updateTexture = true;
      }
      if (oldObjectData.texture !== newObjectData.texture) {
        updateTexture = true;
      }
      if (updateTexture) {
        this.setTexture(newObjectData.texture, this._runtimeScene);
      }
      if (oldObjectData.tiled !== newObjectData.tiled) {
        return false;
      }
      return true;
    }
    getRendererObject() {
      return this._renderer.getRendererObject();
    }
    onDestroyFromScene(runtimeScene) {
      super.onDestroyFromScene(runtimeScene);
      if (this._renderer.onDestroy) {
        this._renderer.onDestroy();
      }
    }
    update() {
      this._renderer.ensureUpToDate();
    }
    extraInitializationFromInitialInstance(initialInstanceData) {
      if (initialInstanceData.customSize) {
        this.setWidth(initialInstanceData.width);
        this.setHeight(initialInstanceData.height);
      }
    }
    setX(x) {
      super.setX(x);
      this._renderer.updatePosition();
    }
    setY(y) {
      super.setY(y);
      this._renderer.updatePosition();
    }
    setTexture(textureName, runtimeScene) {
      this._renderer.setTexture(textureName, runtimeScene);
    }
    setAngle(angle) {
      super.setAngle(angle);
      this._renderer.updateAngle();
    }
    getWidth() {
      return this._width;
    }
    getHeight() {
      return this._height;
    }
    setWidth(width) {
      if (this._width === width)
        return;
      this._width = width;
      this._renderer.updateWidth();
      this.hitBoxesDirty = true;
    }
    setHeight(height) {
      if (this._height === height)
        return;
      this._height = height;
      this._renderer.updateHeight();
      this.hitBoxesDirty = true;
    }
    setOpacity(opacity) {
      if (opacity < 0) {
        opacity = 0;
      }
      if (opacity > 255) {
        opacity = 255;
      }
      this.opacity = opacity;
      this._renderer.updateOpacity();
    }
    getOpacity() {
      return this.opacity;
    }
    setColor(rgbColor) {
      this._renderer.setColor(rgbColor);
    }
    getColor() {
      return this._renderer.getColor();
    }
    getScale() {
      return (this.getScaleX() + this.getScaleY()) / 2;
    }
    getScaleX() {
      return this._width / this._renderer.getTextureWidth();
    }
    getScaleY() {
      return this._height / this._renderer.getTextureHeight();
    }
    setScale(newScale) {
      this.setWidth(this._renderer.getTextureWidth() * newScale);
      this.setHeight(this._renderer.getTextureHeight() * newScale);
    }
    setScaleX(newScale) {
      this.setWidth(this._renderer.getTextureWidth() * newScale);
    }
    setScaleY(newScale) {
      this.setHeight(this._renderer.getTextureHeight() * newScale);
    }
  }
  gdjs2.PanelSpriteRuntimeObject = PanelSpriteRuntimeObject;
  gdjs2.registerObject("PanelSpriteObject::PanelSprite", gdjs2.PanelSpriteRuntimeObject);
})(gdjs || (gdjs = {}));
//# sourceMappingURL=panelspriteruntimeobject.js.map
