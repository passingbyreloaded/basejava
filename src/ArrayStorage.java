import java.util.Arrays;

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
        Resume result = null;
        for (int i = 0; i < size; i++) {
            if (storage[i].toString().equals(uuid)) {
                result = storage[i];
                break;
            }
        }
        return result;
    }

    void delete(String uuid) {
        for (int index = 0; index < size; index++) {
            if (storage[index].toString().equals(uuid)) {
                for (int j = index; j < size - 1; j++) {
                    storage[j] = storage[j + 1];
                }
                storage[size - 1] = null;
                size--;
                break;
            }
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
