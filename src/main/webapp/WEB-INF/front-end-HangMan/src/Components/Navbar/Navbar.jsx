import { Link, Navigate, useNavigate } from "react-router-dom";
import "./Navbar.css";
import { useEffect, useState } from "react";
import AuthContext from "../../utils/AuthContext";
import React, { useContext } from "react";

const Navbar = () => {

    const { isAuthenticated } = useContext(AuthContext);
    const [roles, setRoles] = useState(sessionStorage.getItem('role')?.split(',') || []);
    const navigate = useNavigate();
    const [error, setError] = useState(null);

    const navigateHome = () => {
        navigate('/');
    }
    const updateAuthState = () => {
        const storedRoles = sessionStorage.getItem('role')?.split(',') || [];
        setRoles(storedRoles);
    };

    useEffect(() => {
        updateAuthState();

        window.addEventListener('authChange', updateAuthState);

        return () => {
            window.removeEventListener('authChange', updateAuthState);
        };
    }, []);

    const handleLogOut = () => {
        const token = sessionStorage.getItem('token');
        fetch('http://localhost:8080/api/v1/players/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            }
        }).then((res) => {
            if (res.ok) {
                sessionStorage.removeItem('token');
                sessionStorage.removeItem('username');
                sessionStorage.removeItem('role');
                navigateHome();
            }
        }).catch(err => {
            setError(err);
        })
    };

    return (
        <nav className="nav-bar">
            <a href="/" className="nav-title">
                <h1>Hang Man</h1>
            </a>
            <div className="links">
                <Link to="/">Home</Link>
                <Link to="/statistics">Statistics</Link>
                <Link to="/rankings">Rankings</Link>
                {!isAuthenticated && (
                    <>
                        <Link to="/register" className="btn">Register</Link>
                        <Link to="/login" className="btn">Log In</Link>
                    </>
                )}
                {isAuthenticated && (
                    <>
                        <Link to="/history">History</Link>
                        {roles.includes('ADMIN') &&
                            (<Link to="/admin">Admin</Link>)
                        }
                        <button onClick={handleLogOut} className="btn">Log Out</button>
                    </>
                )}
            </div>
        </nav>
    );
}

export default Navbar;


