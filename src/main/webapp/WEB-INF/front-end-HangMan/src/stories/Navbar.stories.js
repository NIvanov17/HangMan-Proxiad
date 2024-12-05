import React from 'react';
import Navbar from '../Components/Navbar/Navbar';
import { BrowserRouter } from 'react-router-dom';

export default {
    title: 'Components/Navbar',
    component: Navbar
};

const Template = (args) => (
    <BrowserRouter>
        <Navbar {...args} />
    </BrowserRouter>
);

export const Default = Template.bind({});
Default.args = {};