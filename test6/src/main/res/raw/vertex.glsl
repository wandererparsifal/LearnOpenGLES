attribute vec4 vPosition;
uniform mat4 vMatrix;

attribute vec2 vCoordinate;
varying vec2 aCoordinate;

void main(){
    gl_Position=vMatrix*vPosition;
    aCoordinate=vCoordinate;
}
