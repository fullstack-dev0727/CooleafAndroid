package zkhaider.com.cooleaf.ui.revealcolor.action;

/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class PlusAction extends Action {

    public PlusAction() {

        final float bottom = 76f / 96f;
        final float top = 1f - bottom;
        final float left = 20f / 96f;
        final float right = 1f - left;
        final float center = 0.5f;

        lineData = new float[]{
                // line 1
                center, top, center, bottom,
                // line 2
                left, center, right, center,
                // line 3
                center, top, center, bottom,};
    }
}