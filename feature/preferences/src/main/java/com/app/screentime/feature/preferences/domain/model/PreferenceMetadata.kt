package com.app.screentime.feature.preferences.domain.model

data class PreferenceItem(
    val id: String,
    val label: String,
    val emoji: String
)

data class PreferenceCategory(
    val id: String,
    val category: String,
    val emoji: String,
    val items: List<PreferenceItem>
)

data class PreferencesMetadata(
    val interests: List<PreferenceCategory>,
    val occupations: List<PreferenceCategory>,
    val genders: List<PreferenceItem>,
    val religions: List<PreferenceItem>,
    val drinkingOptions: List<PreferenceItem>
) {
    companion object {
        val DEFAULT_GENDERS = listOf(
            PreferenceItem("woman", "Woman", "👩"),
            PreferenceItem("man", "Man", "👨"),
            PreferenceItem("non_binary", "Non-binary", "🌈"),
            PreferenceItem("genderqueer", "Genderqueer", "🦄"),
            PreferenceItem("agender", "Agender", "⚪"),
            PreferenceItem("transgender_woman", "Transgender Woman", "⚧️"),
            PreferenceItem("transgender_man", "Transgender Man", "⚧️"),
            PreferenceItem("other", "Other", "✨")
        )

        val DEFAULT_RELIGIONS = listOf(
            PreferenceItem("hindu", "Hinduism", "🕉️"),
            PreferenceItem("muslim", "Islam", "☪️"),
            PreferenceItem("christian", "Christianity", "✝️"),
            PreferenceItem("sikh", "Sikhism", "ੴ"),
            PreferenceItem("jain", "Jainism", "🛕"),
            PreferenceItem("buddhist", "Buddhism", "☸️"),
            PreferenceItem("parsi", "Parsi / Zoroastrian", "🕯️"),
            PreferenceItem("jewish", "Judaism", "✡️"),
            PreferenceItem("spiritual", "Spiritual", "🕊️"),
            PreferenceItem("agnostic", "Agnostic", "❓"),
            PreferenceItem("atheist", "Atheist", "⚛️"),
            PreferenceItem("other", "Other", "🌟"),
            PreferenceItem("prefer_not_to_say", "Prefer not to say", "🔒")
        )

        val DEFAULT_DRINKING_OPTIONS = listOf(
            PreferenceItem("socially", "Socially / Occasionally", "🍷"),
            PreferenceItem("frequently", "Frequently", "🍻"),
            PreferenceItem("never", "Never / Teetotaler", "🚫"),
            PreferenceItem("trying_to_quit", "Trying to quit", "🛑"),
            PreferenceItem("prefer_not_to_say", "Prefer not to say", "🔒")
        )

        val DEFAULT_INTERESTS = listOf(
            PreferenceCategory(
                id = "food_and_drink",
                category = "Food & Drink",
                emoji = "🍕",
                items = listOf(
                    PreferenceItem("coffee", "Coffee", "☕"),
                    PreferenceItem("chai_tea", "Chai / Tea", "🫖"),
                    PreferenceItem("street_food", "Street Food", "🥘"),
                    PreferenceItem("cooking", "Cooking", "🍳"),
                    PreferenceItem("baking", "Baking", "🧁"),
                    PreferenceItem("fine_dining", "Fine Dining", "🍷"),
                    PreferenceItem("pizza", "Pizza", "🍕"),
                    PreferenceItem("sushi", "Sushi", "🍣"),
                    PreferenceItem("vegan_vegetarian", "Vegan / Vegetarian", "🌱"),
                    PreferenceItem("craft_beer", "Craft Beer", "🍺"),
                    PreferenceItem("boba_bubble_tea", "Boba / Bubble Tea", "🧋")
                )
            ),
            PreferenceCategory(
                id = "travel_and_adventure",
                category = "Travel & Adventure",
                emoji = "✈️",
                items = listOf(
                    PreferenceItem("road_trips", "Road Trips", "🚗"),
                    PreferenceItem("solo_travel", "Solo Travel", "🧳"),
                    PreferenceItem("mountains_trekking", "Mountains / Trekking", "🏔️"),
                    PreferenceItem("beach_vacations", "Beach Vacations", "🏖️"),
                    PreferenceItem("camping", "Camping", "⛺"),
                    PreferenceItem("backpacking", "Backpacking", "🎒"),
                    PreferenceItem("city_breaks", "City Breaks", "🏙️"),
                    PreferenceItem("historical_places", "Historical Places", "🏛️"),
                    PreferenceItem("exploring_cafes", "Exploring Cafes", "☕")
                )
            ),
            PreferenceCategory(
                id = "sports_and_fitness",
                category = "Sports & Fitness",
                emoji = "🏋️",
                items = listOf(
                    PreferenceItem("gym_weightlifting", "Gym / Weightlifting", "🏋️"),
                    PreferenceItem("running_marathon", "Running / Marathon", "🏃"),
                    PreferenceItem("yoga", "Yoga", "🧘"),
                    PreferenceItem("cricket", "Cricket", "🏏"),
                    PreferenceItem("football_soccer", "Football / Soccer", "⚽"),
                    PreferenceItem("badminton", "Badminton", "🏸"),
                    PreferenceItem("cycling", "Cycling", "🚴"),
                    PreferenceItem("swimming", "Swimming", "🏊"),
                    PreferenceItem("pilates", "Pilates", "🤸"),
                    PreferenceItem("boxing_martial_arts", "Boxing / Martial Arts", "🥊"),
                    PreferenceItem("tennis", "Tennis", "🎾")
                )
            ),
            PreferenceCategory(
                id = "entertainment_pop_culture",
                category = "Entertainment & Pop Culture",
                emoji = "🎬",
                items = listOf(
                    PreferenceItem("binge_watching_series", "Binge-Watching Series", "📺"),
                    PreferenceItem("bollywood", "Bollywood", "💃"),
                    PreferenceItem("hollywood_movies", "Hollywood Movies", "🍿"),
                    PreferenceItem("anime_manga", "Anime & Manga", "🎌"),
                    PreferenceItem("standup_comedy", "Stand-up Comedy", "😂"),
                    PreferenceItem("true_crime_podcasts", "True Crime Podcasts", "🎙️"),
                    PreferenceItem("theatre_plays", "Theatre & Plays", "🎭"),
                    PreferenceItem("kdramas", "K-Dramas", "🥢"),
                    PreferenceItem("documentaries", "Documentaries", "📽️")
                )
            ),
            PreferenceCategory(
                id = "music",
                category = "Music",
                emoji = "🎧",
                items = listOf(
                    PreferenceItem("indie_acoustic", "Indie & Acoustic", "🎸"),
                    PreferenceItem("edm_house", "EDM & House", "🎛️"),
                    PreferenceItem("hiphop_rap", "Hip-Hop / Rap", "🎤"),
                    PreferenceItem("punjabi_music", "Punjabi Music", "🪕"),
                    PreferenceItem("live_concerts_gigs", "Live Concerts & Gigs", "🎟️"),
                    PreferenceItem("rock_metal", "Rock & Metal", "⚡"),
                    PreferenceItem("pop_hits", "Pop Hits", "📻"),
                    PreferenceItem("lofi_beats", "Lo-Fi Beats", "🎧"),
                    PreferenceItem("classical_sufi", "Classical / Sufi", "🎻")
                )
            ),
            PreferenceCategory(
                id = "creativity_arts",
                category = "Creativity & Arts",
                emoji = "🎨",
                items = listOf(
                    PreferenceItem("photography", "Photography", "📸"),
                    PreferenceItem("painting_sketching", "Painting / Sketching", "🎨"),
                    PreferenceItem("writing_poetry", "Writing / Poetry", "✍️"),
                    PreferenceItem("dancing", "Dancing", "🕺"),
                    PreferenceItem("fashion_styling", "Fashion & Styling", "👗"),
                    PreferenceItem("interior_design", "Interior Design", "🛋️"),
                    PreferenceItem("diy_crafting", "DIY & Crafting", "✂️"),
                    PreferenceItem("content_creation_vlogging", "Content Creation / Vlogging", "📹")
                )
            ),
            PreferenceCategory(
                id = "gaming_tech",
                category = "Gaming & Tech",
                emoji = "🎮",
                items = listOf(
                    PreferenceItem("pc_console_gaming", "PC / Console Gaming", "🎮"),
                    PreferenceItem("mobile_gaming", "Mobile Gaming (BGMI / Free Fire)", "📱"),
                    PreferenceItem("board_games", "Board Games", "🎲"),
                    PreferenceItem("coding_startups", "Coding & Startups", "💻"),
                    PreferenceItem("scifi_ai", "Sci-Fi & AI", "🤖"),
                    PreferenceItem("chess", "Chess", "♟️"),
                    PreferenceItem("gadgets_electronics", "Gadgets & Electronics", "🔌")
                )
            ),
            PreferenceCategory(
                id = "pets_nature",
                category = "Pets & Nature",
                emoji = "🐾",
                items = listOf(
                    PreferenceItem("dog_lover", "Dog Lover", "🐶"),
                    PreferenceItem("cat_lover", "Cat Lover", "🐱"),
                    PreferenceItem("plant_parent", "Plant Parent", "🪴"),
                    PreferenceItem("wildlife_birds", "Wildlife & Birds", "🦅"),
                    PreferenceItem("stargazing", "Stargazing", "✨"),
                    PreferenceItem("sunset_watching", "Sunset Watching", "🌅")
                )
            ),
            PreferenceCategory(
                id = "lifestyle_values",
                category = "Lifestyle & Values",
                emoji = "🧘",
                items = listOf(
                    PreferenceItem("early_bird", "Early Bird", "🌅"),
                    PreferenceItem("night_owl", "Night Owl", "🦉"),
                    PreferenceItem("spirituality", "Spirituality", "🕉️"),
                    PreferenceItem("meditation_mindfulness", "Meditation & Mindfulness", "🕊️"),
                    PreferenceItem("volunteering_social_causes", "Volunteering & Social Causes", "🤝"),
                    PreferenceItem("minimalist_living", "Minimalist Living", "🌿"),
                    PreferenceItem("astrology_horoscopes", "Astrology & Horoscopes", "🔮")
                )
            )
        )

        val DEFAULT_OCCUPATIONS = listOf(
            PreferenceCategory(
                id = "tech_it_engineering",
                category = "Technology, IT & Engineering",
                emoji = "💻",
                items = listOf(
                    PreferenceItem("software_engineer", "Software Engineer / Developer", "💻"),
                    PreferenceItem("product_manager", "Product Manager", "📱"),
                    PreferenceItem("data_scientist_ai", "Data Scientist / AI Engineer", "🤖"),
                    PreferenceItem("ui_ux_designer", "UI/UX Designer", "🎨"),
                    PreferenceItem("devops_cloud", "DevOps / Cloud Engineer", "☁️"),
                    PreferenceItem("cybersecurity_analyst", "Cybersecurity Analyst", "🛡️"),
                    PreferenceItem("qa_test_engineer", "QA / Test Engineer", "🔍"),
                    PreferenceItem("it_consultant", "IT Consultant", "💼"),
                    PreferenceItem("system_administrator", "System Administrator", "🖥️"),
                    PreferenceItem("hardware_electronics_engineer", "Hardware / Electronics Engineer", "🔌"),
                    PreferenceItem("mechanical_engineer", "Mechanical Engineer", "⚙️"),
                    PreferenceItem("civil_engineer", "Civil Engineer", "🏗️"),
                    PreferenceItem("chemical_engineer", "Chemical Engineer", "🧪")
                )
            ),
            PreferenceCategory(
                id = "business_finance_consulting",
                category = "Business, Finance & Consulting",
                emoji = "💼",
                items = listOf(
                    PreferenceItem("chartered_accountant", "Chartered Accountant (CA)", "📊"),
                    PreferenceItem("investment_banker", "Investment Banker", "🏦"),
                    PreferenceItem("financial_analyst", "Financial Analyst", "📈"),
                    PreferenceItem("management_consultant", "Management Consultant", "💼"),
                    PreferenceItem("business_analyst", "Business Analyst", "📑"),
                    PreferenceItem("vc_angel_investor", "Venture Capitalist / Angel Investor", "💰"),
                    PreferenceItem("stock_trader_wealth_manager", "Stock Trader / Wealth Manager", "💵"),
                    PreferenceItem("auditor_tax_consultant", "Auditor / Tax Consultant", "📜"),
                    PreferenceItem("operations_manager", "Operations Manager", "⚙️"),
                    PreferenceItem("risk_compliance_analyst", "Risk & Compliance Analyst", "⚖️")
                )
            ),
            PreferenceCategory(
                id = "startups_entrepreneurship_freelance",
                category = "Startups, Entrepreneurship & Freelance",
                emoji = "🚀",
                items = listOf(
                    PreferenceItem("founder_cofounder", "Founder / Co-Founder", "🚀"),
                    PreferenceItem("entrepreneur", "Entrepreneur", "💡"),
                    PreferenceItem("freelancer", "Freelancer", "💻"),
                    PreferenceItem("startup_operator", "Startup Operator", "⚡"),
                    PreferenceItem("ecommerce_owner", "E-Commerce Business Owner", "🛒"),
                    PreferenceItem("family_business_owner", "Family Business Owner", "🏬"),
                    PreferenceItem("consultant", "Consultant", "🤝")
                )
            ),
            PreferenceCategory(
                id = "marketing_sales_hr",
                category = "Marketing, Sales & HR",
                emoji = "🏢",
                items = listOf(
                    PreferenceItem("digital_marketer", "Digital Marketer / Growth Hacker", "🚀"),
                    PreferenceItem("brand_manager", "Brand Manager", "🏷️"),
                    PreferenceItem("social_media_manager", "Social Media Manager", "📱"),
                    PreferenceItem("content_strategist_copywriter", "Content Strategist / Copywriter", "✍️"),
                    PreferenceItem("pr_communications", "PR & Communications Specialist", "📢"),
                    PreferenceItem("sales_manager", "Sales Manager / Account Executive", "💼"),
                    PreferenceItem("bd_manager", "Business Development (BD) Manager", "🤝"),
                    PreferenceItem("hr_specialist", "Human Resources (HR) Specialist", "👥"),
                    PreferenceItem("recruiter_talent", "Recruiter / Talent Acquisition", "🎯")
                )
            ),
            PreferenceCategory(
                id = "healthcare_medicine",
                category = "Healthcare & Medicine",
                emoji = "🏥",
                items = listOf(
                    PreferenceItem("doctor_physician", "Doctor / Physician", "🩺"),
                    PreferenceItem("surgeon", "Surgeon", "😷"),
                    PreferenceItem("dentist", "Dentist", "🦷"),
                    PreferenceItem("nurse_healthcare", "Nurse / Healthcare Specialist", "👩‍⚕️"),
                    PreferenceItem("pharmacist", "Pharmacist", "💊"),
                    PreferenceItem("psychologist_counselor", "Psychologist / Counselor", "🧠"),
                    PreferenceItem("physiotherapist", "Physiotherapist", "🦴"),
                    PreferenceItem("nutritionist_dietitian", "Nutritionist / Dietitian", "🥗"),
                    PreferenceItem("veterinarian", "Veterinarian (Vet)", "🐾"),
                    PreferenceItem("medical_researcher", "Medical Researcher", "🔬")
                )
            ),
            PreferenceCategory(
                id = "creative_media_arts",
                category = "Creative, Media, Arts & Entertainment",
                emoji = "🎨",
                items = listOf(
                    PreferenceItem("graphic_designer", "Graphic Designer", "🎨"),
                    PreferenceItem("animator_3d_artist", "Animator / 3D Artist", "🎬"),
                    PreferenceItem("photographer_videographer", "Photographer / Videographer", "📸"),
                    PreferenceItem("film_director", "Film Director / Cinematographer", "🎥"),
                    PreferenceItem("content_creator", "Content Creator / Influencer", "📹"),
                    PreferenceItem("actor_model", "Actor / Model", "🎭"),
                    PreferenceItem("musician_singer", "Musician / Singer / Producer", "🎵"),
                    PreferenceItem("writer_author", "Writer / Author / Journalist", "📰"),
                    PreferenceItem("fashion_designer", "Fashion Designer / Stylist", "👗"),
                    PreferenceItem("interior_designer", "Interior Designer", "🛋️"),
                    PreferenceItem("event_planner", "Event Planner / Producer", "🎉")
                )
            ),
            PreferenceCategory(
                id = "law_governance_civil_services",
                category = "Law, Governance & Civil Services",
                emoji = "⚖️",
                items = listOf(
                    PreferenceItem("lawyer_advocate", "Lawyer / Advocate", "⚖️"),
                    PreferenceItem("corporate_legal_counsel", "Corporate Legal Counsel", "📜"),
                    PreferenceItem("judge_magistrate", "Judge / Magistrate", "👨‍⚖️"),
                    PreferenceItem("civil_servant", "Civil Servant (IAS / IPS / IFS / IRS)", "🏛️"),
                    PreferenceItem("government_officer", "Government Officer", "🇮🇳"),
                    PreferenceItem("public_policy_specialist", "Public Policy Specialist", "📋")
                )
            ),
            PreferenceCategory(
                id = "defense_uniformed_services",
                category = "Defense & Uniformed Services",
                emoji = "🎖️",
                items = listOf(
                    PreferenceItem("army_officer", "Army Officer", "🎖️"),
                    PreferenceItem("navy_officer", "Navy Officer", "⚓"),
                    PreferenceItem("air_force_officer", "Air Force Officer", "✈️"),
                    PreferenceItem("police_officer", "Police Officer", "👮"),
                    PreferenceItem("merchant_navy_captain", "Merchant Navy Officer / Captain", "🚢")
                )
            ),
            PreferenceCategory(
                id = "aviation_travel_hospitality",
                category = "Aviation, Travel & Hospitality",
                emoji = "✈️",
                items = listOf(
                    PreferenceItem("commercial_pilot", "Commercial Pilot", "🧑‍✈️"),
                    PreferenceItem("cabin_crew", "Cabin Crew / Flight Attendant", "✈️"),
                    PreferenceItem("air_traffic_controller", "Air Traffic Controller", "📡"),
                    PreferenceItem("hotel_manager", "Hotel Manager / General Manager", "🏨"),
                    PreferenceItem("chef_culinary", "Chef / Culinary Specialist", "👨‍🍳"),
                    PreferenceItem("travel_consultant", "Travel Consultant / Tour Guide", "🧭")
                )
            ),
            PreferenceCategory(
                id = "education_science_research",
                category = "Education, Science & Research",
                emoji = "🎓",
                items = listOf(
                    PreferenceItem("professor_lecturer", "Professor / Lecturer", "🎓"),
                    PreferenceItem("teacher_educator", "Teacher / Educator", "📚"),
                    PreferenceItem("research_scientist", "Research Scientist", "🔬"),
                    PreferenceItem("phd_scholar", "Ph.D. Scholar / Academician", "📑"),
                    PreferenceItem("school_principal", "School Principal / Administrator", "🏫"),
                    PreferenceItem("career_coach", "Career Coach / Trainer", "🎯")
                )
            ),
            PreferenceCategory(
                id = "architecture_real_estate",
                category = "Architecture, Real Estate & Construction",
                emoji = "🏗️",
                items = listOf(
                    PreferenceItem("architect", "Architect", "📐"),
                    PreferenceItem("urban_planner", "Urban Planner", "🏙️"),
                    PreferenceItem("real_estate_developer", "Real Estate Developer / Agent", "🏢"),
                    PreferenceItem("construction_manager", "Construction Manager", "👷"),
                    PreferenceItem("landscape_designer", "Landscape Designer", "🏡")
                )
            ),
            PreferenceCategory(
                id = "student_non_corporate",
                category = "Student & Non-Corporate",
                emoji = "🎓",
                items = listOf(
                    PreferenceItem("student", "Student / College Student", "🎒"),
                    PreferenceItem("graduate_student", "Graduate Student (Master's / Ph.D.)", "🎓"),
                    PreferenceItem("fitness_personal_trainer", "Working in Fitness / Personal Trainer", "🏋️"),
                    PreferenceItem("yoga_instructor", "Yoga Instructor", "🧘"),
                    PreferenceItem("professional_athlete", "Professional Athlete / Sports Coach", "⚽"),
                    PreferenceItem("homemaker", "Homemaker", "🏡"),
                    PreferenceItem("in_between_jobs", "In Between Jobs / Taking a Break", "☕")
                )
            )
        )

        val DEFAULT = PreferencesMetadata(
            interests = DEFAULT_INTERESTS,
            occupations = DEFAULT_OCCUPATIONS,
            genders = DEFAULT_GENDERS,
            religions = DEFAULT_RELIGIONS,
            drinkingOptions = DEFAULT_DRINKING_OPTIONS
        )
    }
}
