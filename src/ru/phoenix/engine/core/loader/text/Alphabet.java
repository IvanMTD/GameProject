package ru.phoenix.engine.core.loader.text;

import ru.phoenix.engine.core.buffer.template.ObjectConfiguration;
import ru.phoenix.engine.core.configuration.WindowConfig;
import ru.phoenix.engine.core.loader.texture.Texture;
import ru.phoenix.engine.core.loader.texture.Texture2D;
import ru.phoenix.engine.core.shader.Shader;
import ru.phoenix.engine.math.variable.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.engine.core.constants.System.ENGLISH_LANGUAGE;
import static ru.phoenix.engine.core.constants.System.RUSSIAN_LANGUAGE;
import static ru.phoenix.engine.core.constants.TextureInfo.ALPHABET_ENGLISH;
import static ru.phoenix.engine.core.constants.TextureInfo.ALPHABET_RUSSIAN;

public class Alphabet {

    private static Alphabet instance = null;

    private final float LETTER_OFFSET   = 140.0f;

    public final int LEFT_ALIGNMENT     = 100;
    public final int RIGHT_ALIGNMENT    = 101;
    public final int CENTER_ALIGNMENT   = 102;

    private Texture russianTexture;
    private Texture englishTexture;
    private Texture texture;
    private Map<Character,Letter> alphabetMapRus;
    private Map<Character,Letter> alphabetMapEn;
    private Map<Character,Letter> alphabet;

    public Alphabet(){
        russianTexture  = new Texture2D();
        englishTexture  = new Texture2D();
        texture = new Texture2D();
        alphabetMapRus  = new HashMap<>();
        alphabetMapEn   = new HashMap<>();
        alphabet = new HashMap<>();
        init();
    }

    private void init(){
        initTexture();
        initRussianLanguage();
        initEnglishLanguage();
        setCurrentLanguage();
    }

    private void initTexture(){
        russianTexture.setup(ALPHABET_RUSSIAN,GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        englishTexture.setup(ALPHABET_ENGLISH,GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
    }

    private void initRussianLanguage(){
        int column  = 13;
        int row     = 7;
        char[] symbols = new char[]{
                'А','Б','В','Г','Д','Е','Ё','Ж','З','И','Й','К','Л',
                'М','Н','О','П','Р','С','Т','У','Ф','Х','Ц','Ч','Ш',
                'Щ','Ъ','Ы','Ь','Э','Ю','Я','а','б','в','г','д','е',
                'ё','ж','з','и','й','к','л','м','н','о','п','р','с',
                'т','у','ф','х','ц','ч','ш','щ','ъ','ы','ь','э','ю',
                'я','1','2','3','4','5','6','7','8','9','0','.',',',
                '!','?','(',')','\'','"','/','|','\\',':',';',' ','\n'
        };
        initLanguage(alphabetMapRus,symbols,column,row,russianTexture.getWidth(),russianTexture.getHeight());
    }

    private void initEnglishLanguage(){
        int column  = 13;
        int row     = 6;
        char[] symbols = new char[]{
                'A','B','C','D','E','F','G','H','I','J','K','L','M',
                'N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                'a','b','c','d','e','f','g','h','i','j','k','l','m',
                'n','o','p','q','r','s','t','u','v','w','x','y','z',
                '1','2','3','4','5','6','7','8','9','0','.',',','!',
                '?','(',')','\'','"','/','|','\\',':',';',' ','\n','\b'
        };
        initLanguage(alphabetMapEn,symbols,column,row,englishTexture.getWidth(),englishTexture.getHeight());
    }

    private void initLanguage(Map<Character,Letter> langMap, char[] symbols, int column, int row, int width, int height){

        float x = LETTER_OFFSET / 2.0f;
        float y = LETTER_OFFSET / 2.0f;
        float offsetX = LETTER_OFFSET / width;
        float offsetY = LETTER_OFFSET / height;

        int index = 0;

        for(int r = 1; r <= row; r++){
            for(int c = 1; c <= column; c++){
                float[] pos = new float[]{
                        -x,  y, 0.0f,
                        -x, -y, 0.0f,
                         x, -y, 0.0f,
                         x,  y, 0.0f
                };

                float tx = (LETTER_OFFSET * c) / width;
                float ty = (LETTER_OFFSET * r) / height;

                float[] tex = new float[]{
                        tx - offsetX,  ty,
                        tx - offsetX,  ty - offsetY,
                        tx,            ty - offsetY,
                        tx,            ty
                };

                int[] indices = new int[]{
                        0,1,2,
                        0,2,3
                };

                ObjectConfiguration objectConfiguration = new ObjectConfiguration(pos, tex, indices);
                Letter letter = new Letter();
                letter.init(new Vector3f(), objectConfiguration);
                langMap.put(symbols[index],letter);

                index++;
            }
        }
    }

    public void setCurrentLanguage(){
        if(WindowConfig.getInstance().getLanguage() == RUSSIAN_LANGUAGE){
            texture = russianTexture;
            alphabet = alphabetMapRus;
        }else if(WindowConfig.getInstance().getLanguage() == ENGLISH_LANGUAGE){
            texture = englishTexture;
            alphabet = alphabetMapEn;
        }
    }

    public void drawText(String text, Shader shader, int printingType, Vector3f position, float letterSize, float distanceBetweenLetters){
        setUniforms(shader);
        char[] symbols = text.toCharArray();
        Vector3f positionOffset = new Vector3f(position);
        if(printingType == LEFT_ALIGNMENT){
            for(Character symbol : symbols){
                Letter letter = alphabet.get(symbol);
                letter.update(positionOffset,letterSize);
                letter.draw(shader);
                positionOffset = new Vector3f(positionOffset.add(new Vector3f(distanceBetweenLetters,0.0f,0.0f)));
            }
        }else if(printingType == CENTER_ALIGNMENT){
            float offset = (symbols.length / 2.5f) * distanceBetweenLetters;
            positionOffset = new Vector3f(position.getX() - offset, position.getY(), position.getZ());
            for(Character symbol : symbols) {
                Letter letter = alphabet.get(symbol);
                letter.update(positionOffset, letterSize);
                letter.draw(shader);
                positionOffset = new Vector3f(positionOffset.add(new Vector3f(distanceBetweenLetters, 0.0f, 0.0f)));
            }
        }else if(printingType == RIGHT_ALIGNMENT){
            for(Character symbol : symbols){
                Letter letter = alphabet.get(symbol);
                letter.update(positionOffset,letterSize);
                letter.draw(shader);
                positionOffset = new Vector3f(positionOffset.add(new Vector3f(positionOffset.getX() - distanceBetweenLetters,0.0f,0.0f)));
            }
        }
    }

    private void setUniforms(Shader shader){
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        shader.setUniform("image", 0);
    }

    public float getLetterOffset(){
        return LETTER_OFFSET;
    }

    public static Alphabet getInstance(){
        if(instance == null){
            instance = new Alphabet();
        }
        return instance;
    }
}
