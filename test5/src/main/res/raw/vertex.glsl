attribute vec4 vPosition;
uniform mat4 vMatrix;

attribute vec2 vCoordinate;
varying vec2 aCoordinate;

attribute vec2 vCoordinate2;
varying vec2 aCoordinate2;

void main(){
    gl_Position=vMatrix*vPosition;
    aCoordinate=vCoordinate;
    aCoordinate2=vCoordinate2;
}
