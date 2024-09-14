export const ACCESS_TOKEN = "ACCESS-TOKEN";
// saveToken
export const saveToken = (token) => localStorage.setItem(ACCESS_TOKEN, token);

// getToken
export const getToken = () => localStorage.getItem(ACCESS_TOKEN);

// removeToken
export const removeToken = () => localStorage.removeItem(ACCESS_TOKEN);
