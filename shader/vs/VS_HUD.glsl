#version 430 core

layout (location = 0) in vec3 pos;
layout (location = 1) in vec3 norm;
layout (location = 2) in vec2 tex;
layout (location = 3) in vec3 tan;
layout (location = 4) in vec3 bi_tan;
layout (location = 5) in ivec4 bone_id;
layout (location = 6) in vec4 bone_w;
layout (location = 7) in mat4 instance_m;

layout (row_major, std140) uniform matrices{
    mat4 perspective_m;
    mat4 ortho_m;
    mat4 view_m;
};

out vec2 texture_coord;

uniform mat4 model_m;

void main() {
    gl_Position = ortho_m * model_m * vec4(pos,1.0f);
    texture_coord = tex;
}
