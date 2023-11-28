package audio.slice;

import java.util.List;

/**
 * x.z
 * Create in 2023/5/15
 */
public class VadResult {

    private SliceResult results;

    public SliceResult getResults() {
        return results;
    }

    public void setResults(SliceResult results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "VadResult{" +
                "results=" + results +
                '}';
    }

    class SliceResult{
        private List<VadSecond> speech_timestamps_seconds;
        private List<String> chunks_name;

        public List<VadSecond> getSpeech_timestamps_seconds() {
            return speech_timestamps_seconds;
        }

        public void setSpeech_timestamps_seconds(List<VadSecond> speech_timestamps_seconds) {
            this.speech_timestamps_seconds = speech_timestamps_seconds;
        }

        public List<String> getChunks_name() {
            return chunks_name;
        }

        public void setChunks_name(List<String> chunks_name) {
            this.chunks_name = chunks_name;
        }

        @Override
        public String toString() {
            return "SliceResult{" +
                    "speech_timestamps_seconds=" + speech_timestamps_seconds +
                    ", chunks_name=" + chunks_name +
                    '}';
        }
    }

    class VadSecond{
        private Double start;
        private Double end;

        public Double getStart() {
            return start;
        }

        public void setStart(Double start) {
            this.start = start;
        }

        public Double getEnd() {
            return end;
        }

        public void setEnd(Double end) {
            this.end = end;
        }

        @Override
        public String toString() {
            return "VadSecond{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }
    }
}
