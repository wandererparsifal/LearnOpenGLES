#extension GL_OES_EGL_image_external : require
precision mediump float;

uniform samplerExternalOES vTexture;
varying vec2 aCoordinate;

uniform sampler2D vTexture2;
varying vec2 aCoordinate2;

void main() {
    vec4 texture1 = texture2D(vTexture,aCoordinate);
    vec4 texture2 = texture2D(vTexture2, aCoordinate2);
    gl_FragColor=texture1+texture2;
}
