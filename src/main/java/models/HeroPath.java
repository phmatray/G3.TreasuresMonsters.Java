package models;

public class HeroPath {
    private final String path;
    private final String normalizedPath;

    public HeroPath(String path) {
        this.path = path;
        this.normalizedPath = simplifyPath(path);
    }

    public String getPath() { return path; }
    public String getNormalizedPath() { return normalizedPath; }

    private static String simplifyPath(String path) {
        if (path == null || path.isEmpty()) {
            return "";
        }

        StringBuilder simplifiedPath = new StringBuilder();
        int count = 1;

        for (int i = 1; i < path.length(); i++) {
            if (path.charAt(i) == path.charAt(i - 1)) {
                count++;
            } else {
                simplifiedPath.append(path.charAt(i - 1));
                if (count > 1) {
                    simplifiedPath.append(count);
                }
                simplifiedPath.append(' ');
                count = 1;
            }
        }

        simplifiedPath.append(path.charAt(path.length() - 1));
        if (count > 1) {
            simplifiedPath.append(count);
        }

        return simplifiedPath.toString().trim();
    }
}
