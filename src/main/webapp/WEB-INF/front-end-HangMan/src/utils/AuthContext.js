import React, { createContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const navigate = useNavigate();

    const validateToken = () => {
        try {
            const token = sessionStorage.getItem("token");
            if (!token) {
                setIsAuthenticated(false);
                return;
            }

            const payload = JSON.parse(atob(token.split(".")[1]));
            const isExpired = payload.exp * 1000 < Date.now();

            if (isExpired) {
                sessionStorage.removeItem("token");
                sessionStorage.removeItem("username");
                sessionStorage.removeItem("role");
                setIsAuthenticated(false);
                return;
            } else {
                setIsAuthenticated(true);
            }
        } catch (error) {
            console.log("in the catch block!")
            sessionStorage.clear();
            setIsAuthenticated(false);
            navigate("/login");
            window.location.reload();
        }
    }

    useEffect(() => {
        validateToken();
    }, [navigate]);

    return (
        <AuthContext.Provider value={{ isAuthenticated }}>
            {children}
        </AuthContext.Provider>
    );
}

export default AuthContext;