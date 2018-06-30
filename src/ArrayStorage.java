import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int size;

    void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    void save(Resume r) {
        storage[size] = r;
        size++;
    }

    Resume get(String uuid) {
        return Arrays.stream(storage, 0, size)
                .filter(r -> r.toString().equals(uuid))
                .findFirst().orElse(null);
    }

    void delete(String uuid) {
        int index = IntStream.range(0, size)
                .filter(i -> uuid.equals(storage[i].toString()))
                .findFirst().orElse(-1);

        if (index > -1) {
            IntStream.range(index, size - 1).forEach(i -> storage[i] = storage[i + 1]);
            storage[size - 1] = null;
            size--;
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    int size() {
        return size;
    }
}
