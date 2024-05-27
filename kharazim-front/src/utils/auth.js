export const ACCESS_TOKEN = "ACCESS-TOKEN";
// saveToken
export const saveToken = (token) => {
    sessionStorage.setItem(ACCESS_TOKEN, token);
}

// getToken
export const getToken = () => sessionStorage.getItem(ACCESS_TOKEN);

// removeToken
export const removeToken = () => sessionStorage.removeItem(ACCESS_TOKEN);
