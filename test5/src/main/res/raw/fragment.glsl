precision mediump float;

uniform sampler2D vTexture;
varying vec2 aCoordinate;

uniform sampler2D vTexture2;
varying vec2 aCoordinate2;

void main(){
    vec4 texture1 = texture2D(vTexture, aCoordinate);
    vec4 texture2 = texture2D(vTexture2, aCoordinate2);
    vec4 vv = vec4(0.0,0.0,0.0,0.0);
    if (texture2 == vv) {
        gl_FragColor=texture1;
    } else {
        gl_FragColor=texture2;
    }
}
