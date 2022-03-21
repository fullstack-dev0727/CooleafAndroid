package zkhaider.com.cooleaf.ui.picassotransformations.wasabeef;

/**
 * Copyright (C) 2015 Wasabeef
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.squareup.picasso.Transformation;

import android.content.Context;
import android.graphics.Bitmap;

import zkhaider.com.cooleaf.ui.picassotransformations.cyberagent.GPUImage;
import zkhaider.com.cooleaf.ui.picassotransformations.cyberagent.GPUImageBrightnessFilter;


/**
 * brightness value ranges from -1.0 to 1.0, with 0.0 as the normal level
 */
public class BrightnessFilterTransformation implements Transformation {

    private Context mContext;

    private GPUImageBrightnessFilter mFilter = new GPUImageBrightnessFilter();
    private float mBrightness;

    public BrightnessFilterTransformation(Context context) {
        mContext = context;
    }

    public BrightnessFilterTransformation(Context context, float brightness) {
        mContext = context;
        mBrightness = brightness;
        mFilter.setBrightness(mBrightness);
    }

    @Override
    public Bitmap transform(Bitmap source) {

        GPUImage gpuImage = new GPUImage(mContext);
        gpuImage.setImage(source);
        gpuImage.setFilter(mFilter);
        Bitmap bitmap = gpuImage.getBitmapWithFilterApplied();

        source.recycle();

        return bitmap;
    }

    @Override
    public String key() {
        return "BrightnessFilterTransformation(brightness=" + mBrightness + ")";
    }
}