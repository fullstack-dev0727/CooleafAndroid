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

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A LineSegment describes which lines within an Action are linked together
 */
public class LineSegment implements Parcelable {

    public int[] indexes;

    public LineSegment(int... indexes) {
        this.indexes = indexes;
    }

    public int getStartIdx() {
        return indexes[0];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(this.indexes);
    }

    private LineSegment(Parcel in) {
        this.indexes = in.createIntArray();
    }

    public static final Creator<LineSegment> CREATOR = new Creator<LineSegment>() {
        public LineSegment createFromParcel(Parcel source) {
            return new LineSegment(source);
        }

        public LineSegment[] newArray(int size) {
            return new LineSegment[size];
        }
    };
}