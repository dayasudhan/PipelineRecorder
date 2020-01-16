package com.kuruvatech.pipelinerecorder.Loader.render;

import android.content.Context;
import android.util.SparseArray;

import com.kuruvatech.pipelinerecorder.Loader.render.LoadingRenderer;

import java.lang.reflect.Constructor;
import khaanavali.customer.Loader.render.circle.jump.GuardLoadingRenderer;
public final class LoadingRendererFactory {
    private static final SparseArray<Class<? extends LoadingRenderer>> LOADING_RENDERERS = new SparseArray<>();

    static {

        //circle jump
         LOADING_RENDERERS.put(5, GuardLoadingRenderer.class);
    }

    private LoadingRendererFactory() {
    }

    public static LoadingRenderer createLoadingRenderer(Context context, int loadingRendererId) throws Exception {
        Class<?> loadingRendererClazz = LOADING_RENDERERS.get(loadingRendererId);
        Constructor<?>[] constructors = loadingRendererClazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes != null
                    && parameterTypes.length == 1
                    && parameterTypes[0].equals(Context.class)) {
                constructor.setAccessible(true);
                return (LoadingRenderer) constructor.newInstance(context);
            }
        }

        throw new InstantiationException();
    }
}
