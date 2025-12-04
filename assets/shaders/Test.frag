#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif
varying LOWP vec4 v_color;
varying vec2 v_texCoords;
varying float v_texture_index;
uniform sampler2D u_textures[MAX_TEXTURE_UNITS];

<GET_TEXTURE_FROM_ARRAY_PLACEHOLDER>

void main() {
    gl_FragColor = v_color * getTextureFromArray(v_texCoords);
}
