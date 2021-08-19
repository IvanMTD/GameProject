#version 430 core

#define KERNEL_STD 0
#define KERNEL_BLR 1
#define KERNEL_INV 2
#define KERNEL_SHR 3

out vec4 fragment_color;

in vec2 texture_coord;

uniform sampler2D main_texture;
uniform float gamma;
uniform float contrast;
uniform float tune;

uniform int kernel;
uniform float correction;

float[9] getKernel();

void main(){

    float offset = 1.0 / correction;

    vec2 offsets[9] = vec2[](
    vec2(-offset,  offset), // top-left
    vec2( 0.0f,    offset), // top-center
    vec2( offset,  offset), // top-right
    vec2(-offset,  0.0f),   // center-left
    vec2( 0.0f,    0.0f),   // center-center
    vec2( offset,  0.0f),   // center-right
    vec2(-offset, -offset), // bottom-left
    vec2( 0.0f,   -offset), // bottom-center
    vec2( offset, -offset)  // bottom-right
    );

    float kernel_main[9] = getKernel();

    vec3 sampleTex[9];

    for(int i = 0; i < 9; i++){
        sampleTex[i] = vec3(texture(main_texture, texture_coord.st + offsets[i]));
    }

    vec3 col = vec3(0.0);

    for(int i = 0; i < 9; i++){
        col += sampleTex[i] * kernel_main[i];
    }

    // hdr
    vec3 hdrColor = col.rgb;
    // gamma correction
    vec3 mapped = pow(hdrColor, vec3(1.0 / gamma));
    // contrast corection
    mapped.rgb = (mapped.rgb - 0.5f) * (1.0f + contrast) + 0.5f;

    fragment_color = vec4(mapped * tune, 1.0);
}

float[9] getKernel(){
    float kernel_main[9];

    if(kernel == KERNEL_STD){
        float kernel_std[9] = float[](
        0,0,0,
        0,1,0,
        0,0,0
        );
        kernel_main = kernel_std;
    }else if(kernel == KERNEL_BLR){
        float kernel_blur[9] = float[]( // Ядро - размытия
        1.0 / 16, 2.0 / 16, 1.0 / 16,
        2.0 / 16, 4.0 / 16, 2.0 / 16,
        1.0 / 16, 2.0 / 16, 1.0 / 16
        );
        kernel_main = kernel_blur;
    }else if(kernel == KERNEL_INV){
        float kernel_inverse[9] = float[](
        1, 1, 1,
        1,-8, 1,
        1, 1, 1
        );
        kernel_main = kernel_inverse;
    }else if(kernel == KERNEL_SHR){
        float kernel_sharpness[9] = float[]( // Ядро - резкости
        -1, -1, -1,
        -1,  9, -1,
        -1, -1, -1
        );
        kernel_main = kernel_sharpness;
    }else{
        float kernel_std[9] = float[](
        0,0,0,
        0,1,0,
        0,0,0
        );
        kernel_main = kernel_std;
    }

    return kernel_main;
}