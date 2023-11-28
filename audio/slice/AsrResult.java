package audio.slice;

import java.util.List;
import java.util.Map;

/**
 * x.z
 * Create in 2023/5/8
 */
public class AsrResult {
    private List<Transcribe> results;

    public List<Transcribe> getResults() {
        return results;
    }

    public void setResults(List<Transcribe> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "AsrResult{" +
                "results=" + results +
                '}';
    }

    class Transcribe{
        private    String filename;
        private String result ;
        private Double rtf;

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public Double getRtf() {
            return rtf;
        }

        public void setRtf(Double rtf) {
            this.rtf = rtf;
        }

        @Override
        public String toString() {
            return "Transcribe{" +
                    "filename='" + filename + '\'' +
                    ", result='" + result + '\'' +
                    ", rtf=" + rtf +
                    '}';
        }
    }
}
