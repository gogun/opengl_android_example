precision mediump float;

uniform sampler2D u_TextureUnit;
varying vec2 v_Texture;

void main() {
    vec4 color = texture2D(u_TextureUnit, v_Texture);
    gl_FragColor = color;
}