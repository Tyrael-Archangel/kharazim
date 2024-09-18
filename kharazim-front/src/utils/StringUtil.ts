export function join(
  elements: string[],
  delimiter: string | null,
  prefix: string | null,
  suffix: string | null,
): string | null {
  if (!elements || elements.length == 0) {
    return "";
  }
  let result: string = elements[0];
  for (let i = 1; i < elements.length; i++) {
    if (delimiter) {
      result += delimiter + elements[i];
    } else {
      result += elements[i];
    }
  }
  if (prefix) {
    result = prefix + result;
  }
  if (suffix) {
    result += suffix;
  }
  return result;
}

export function ifNullEmpty(str: string): string {
  return str ? str : "";
}