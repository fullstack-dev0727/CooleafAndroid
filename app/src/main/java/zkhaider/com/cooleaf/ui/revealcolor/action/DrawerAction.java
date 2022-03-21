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

public class DrawerAction extends Action {

    public DrawerAction() {

        final float startX = 0.1375f;
        final float endX = 1f - startX;
        final float endY = 0.707f;
        final float startY = 1f - endY;
        final float center = 0.5f;

        lineData = new float[]{
                // line 1
                startX, startY, endX, startY,
                // line 2
                startX, center, endX, center,
                // line 3
                startX, endY, endX, endY,};
    }
}