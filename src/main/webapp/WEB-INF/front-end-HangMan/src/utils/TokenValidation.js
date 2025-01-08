export const validateToken = (res, navigate) => {
    if (res.status === 401) {
        sessionStorage.removeItem("token");
        sessionStorage.removeItem("username");
        sessionStorage.removeItem("role");
        navigate("/login");
        return Promise.reject(new Error("Unauthorized"));
    }
    return res.json();
};