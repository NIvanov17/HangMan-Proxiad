import { Link } from "react-router-dom";
import "./Navbar.css";

const Navbar = () => {

    return (
        <nav className="nav-bar">
            <a href="/" className="nav-title">
                <h1>Hang Man</h1>
            </a>
            <div className="links">
                <Link to="/">Home</Link>
                <Link to="/history/log-in">History</Link>
                <Link to="/statistics">Statistics</Link>
                <Link to="/rankings">Rankings</Link>
                <Link to="/register">Register</Link>
                <Link to="/login">Log In</Link>
                <Link to="/logout">Log Out</Link>
                <Link to="/admin">Admin</Link>
            </div>
        </nav>
    );
}

export default Navbar;