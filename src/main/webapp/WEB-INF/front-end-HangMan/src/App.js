import Navbar from './Navbar';
import Home from './Home';
import Footer from './Footer';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Login from './Login';
import SinglePlayerGame from './Single-PlayerGame';
import MultiPlayerGame from './MultiPlayerGame';
import LoginGiver from './LoginGiver';
import LoginGuesser from './LoginGuesser';
import WordToGuess from './WordsToGuess';
import History from './History';
import { useState } from "react";
import HistoryLogIn from './HistoryLogIn';

function App() {

  const [isAuthenticated, setIsAuthenticated] = useState(false);

  return (
    <Router>
      <div className="App">
        <Navbar />
        <div className="content">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/single-player/username" element={<Login />} />
            <Route path='/single-player/games' element={<SinglePlayerGame />} />
            <Route path="/multi-player/guesser" element={<LoginGuesser />} />
            <Route path="/multi-player/giver" element={<LoginGiver />} />
            <Route path='/multi-player/words' element={<WordToGuess />} />
            <Route path='/multi-player/games' element={<MultiPlayerGame />} />
            <Route path='/history/log-in' element={<HistoryLogIn />} />
            <Route path='/history' element={<History />} />
          </Routes>
        </div>
        <Footer />
      </div>
    </Router>
  );
}

export default App;
