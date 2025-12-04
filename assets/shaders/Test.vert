attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
attribute float a_texture_index;
uniform mat4 u_projTrans;
varying vec4 v_color;
varying vec2 v_texCoords;
varying float v_texture_index;

void main() {
    v_color = a_color;
    v_color.a = v_color.a * (255.0/254.0);
    v_texCoords = a_texCoord0;
    v_texture_index = a_texture_index;
    gl_Position =  u_projTrans * a_position;
}
