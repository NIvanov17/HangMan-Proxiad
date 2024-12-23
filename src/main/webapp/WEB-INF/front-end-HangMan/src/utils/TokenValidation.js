export const validateToken = (res, navigate) => {
    if (res.status === 401) {
        console.log('ex thrown');
        sessionStorage.removeItem("token");
        sessionStorage.removeItem("username");
        navigate("/login");
        return Promise.reject(new Error("Unauthorized"));
    }
    return res.json();
};