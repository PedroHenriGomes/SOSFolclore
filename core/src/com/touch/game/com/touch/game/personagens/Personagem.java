package com.touch.game.com.touch.game.personagens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.touch.game.com.touch.game.interfaces.Ser;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static com.touch.game.Constantes.*;


public abstract class Personagem implements Ser {

    Circle corpo;

    Texture[] frames;

    Texture personagemParado;

    float auxiliarDeFrames;

    Vector2 velocidade;

    boolean andando = false;

    boolean atacando = false;

    boolean pulando = false;

    int quantidadeDeTexturas, posx, posy;

    public Personagem(Texture personaParado ,int posx, int posy, int quantidadeDeTexturasInicial) {
        this.posx = posx;
        this.posy = posy;
        this.quantidadeDeTexturas = quantidadeDeTexturasInicial;
        velocidade = new Vector2(0,0);
        velocidade.y = -impulso;

        personagemParado = personaParado;
        corpo = new Circle(posx, posy, personagem);

    }

    protected Personagem() {
    }

    public abstract Texture getMovimentacaoPersonagem(int i) throws NotImplementedException;

    public abstract Texture getAtackPersonagem(int i) throws NotImplementedException;

    public abstract Texture getPuloPersonagem(int i) throws NotImplementedException;

    public void draw(SpriteBatch batch) {

        if (andando | atacando) {
            batch.draw(frames[(int)auxiliarDeFrames % quantidadeDeTexturas], posx, posy,
                    corpo.radius * 2, corpo.radius*2);
        } else if (pulando) {

        } else {
            batch.draw(personagemParado, posx, posy, corpo.radius * 2, corpo.radius*2);
        }

    }

    public void update(int quantidadeDeTexturas ,float time, boolean movimentando, boolean atacou, boolean pulando) {
        this.andando = movimentando;
        this.atacando = atacou;


        if(andando) {
            this.quantidadeDeTexturas = 3;
            andar();
        }

        if(atacando) {
            this.quantidadeDeTexturas = 2;
            atacar();
        }

        auxiliarDeFrames += this.quantidadeDeTexturas*time;
        corpo.x += velocidade.x * time;
        corpo.y += velocidade.y * time;

        velocidade.y -= gravidade * time;

        if (corpo.y + corpo.radius > SCREENY) {
            corpo.y = SCREENY - corpo.radius;
            velocidade.y = -impulso;
        }

        if (corpo.y - corpo.radius <= SCREENY) {
            corpo.y = corpo.radius;
            velocidade.y = impulso;
        }
    }

    public void impulso() {
        velocidade.y += impulso;
    }

    private void andar() {
        corpo = new Circle(posx, posy, personagem);
        frames = new Texture[quantidadeDeTexturas];

        for (int i =1; i <= quantidadeDeTexturas;i++) {
            frames[i-1] = getMovimentacaoPersonagem(i);
        }
    }


    private void atacar() {
        corpo = new Circle(posx, posy, personagem);
        frames = new Texture[quantidadeDeTexturas];

        for (int i = 1; i <= quantidadeDeTexturas; i++) {
            frames[i-1] = getAtackPersonagem(i);
        }
    }

    @Override
    public void caminhar() {
        andar();
    }

    public void dispose() {
        for (int i = 0;i<= quantidadeDeTexturas;i++) {
           frames[i].dispose();
        }
    }

    public Circle getCorpoPersonagem() {
        return corpo;
    }
}
