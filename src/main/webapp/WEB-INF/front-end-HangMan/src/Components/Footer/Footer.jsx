import "./Footer.css";

const Footer = () => {

    const link = 'https://gitlab.sc.proxiad.bg/n.ivanov/hangman-niki-ivanov';

    return (

        <div className="footer">
            <p>Created By Nikolay Ivanov.</p>
            <a href={link}>View Source Code</a>
        </div>
    );
}

export default Footer;