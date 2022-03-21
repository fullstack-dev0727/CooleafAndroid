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

public class CloseAction extends Action {

    public CloseAction() {
        final float start = 0.239375f;
        final float end = 1f - start;

        lineData = new float[]{
                // line 1
                start, start, end, end,
                // line 2
                start, end, end, start,
                // line 3
                start, start, end, end,};
    }
}