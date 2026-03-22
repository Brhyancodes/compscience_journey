# Program to read dictionary items from a file,
# invert the dictionary, and write the inverted dictionary to another file.
# The program also demonstrates exception handling for file operations.


def read_dictionary(file_name):
    """
    Reads key:value pairs from a file and stores them in a dictionary.
    """
    dictionary = {}

    try:
        with open(file_name, "r") as file:
            for line in file:
                line = line.strip()

                if line:
                    key, value = line.split(":")
                    dictionary[key.strip()] = value.strip()

    except FileNotFoundError:
        print("Error: Input file not found.")
    except Exception as e:
        print("An error occurred:", e)

    return dictionary


def invert_dictionary(original_dict):
    """
    Inverts the dictionary so values become keys and keys become values.
    Handles multiple values separated by commas.
    """
    inverted = {}

    for key, value in original_dict.items():

        colors = value.split(",")

        for color in colors:
            color = color.strip()

            if color not in inverted:
                inverted[color] = []

            inverted[color].append(key)

    return inverted


def write_dictionary(file_name, dictionary):
    """
    Writes dictionary content into a file.
    """

    try:
        with open(file_name, "w") as file:

            for key, values in dictionary.items():
                line = key + ":" + ", ".join(values) + "\n"
                file.write(line)

    except IOError:
        print("Error writing to file.")


# Main Program

input_file = "original_dictionary.txt"
output_file = "inverted_dictionary.txt"

original_dict = read_dictionary(input_file)

inverted_dict = invert_dictionary(original_dict)

write_dictionary(output_file, inverted_dict)

print("Original Dictionary:")
print(original_dict)

print("\nInverted Dictionary:")
print(inverted_dict)
